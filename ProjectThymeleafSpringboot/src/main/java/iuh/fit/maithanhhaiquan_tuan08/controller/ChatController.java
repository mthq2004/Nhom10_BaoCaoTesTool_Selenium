package iuh.fit.maithanhhaiquan_tuan08.controller;

import iuh.fit.maithanhhaiquan_tuan08.entity.Product;
import iuh.fit.maithanhhaiquan_tuan08.service.ProductService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = { "http://localhost:5173", "http://127.0.0.1:5173" })
public class ChatController {

    private final GoogleGenAiChatModel chatModel;
    private final ChatClient chatClient;
    private final ProductService productService;

    public ChatController(GoogleGenAiChatModel chatModel, ProductService productService) {
        this.chatModel = chatModel;
        this.chatClient = ChatClient.create(chatModel);
        this.productService = productService;
    }

    /**
     * Cách 1: Gọi trực tiếp với GoogleGenAiChatModel
     * GET /api/ai/generate?message=Kể 1 câu đùa vui
     */
    @GetMapping("/generate")
    public Map<String, Object> generate(
            @RequestParam(defaultValue = "Kể 1 câu đùa vui") String message) {
        ChatResponse res = chatModel.call(new Prompt(message));
        String output = res.getResult().getOutput().getText();
        return Map.of(
                "input", message,
                "output", output);
    }

    /**
     * Cách 2: Dùng ChatClient builder API
     * GET /api/ai/ask?q=Bạn là ai?
     */
    @GetMapping("/ask")
    public Map<String, String> ask(@RequestParam String q) {
        String response = chatClient.prompt(q).call().content();
        return Map.of(
                "question", q,
                "answer", response);
    }

    /**
     * Stream đáp án bằng Server-Sent Events
     * GET /api/ai/stream?message=Giải thích AI là gì
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> stream(@RequestParam String message) {
        return chatModel.stream(new Prompt(message))
                .map(chatResponse -> chatResponse.getResult().getOutput().getText());
    }

    /**
     * Chat đơn giản: POST với JSON body
     * POST /api/ai/chat
     * Body: {"message": "Bạn tên gì?"}
     */
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "Hello");
        try {
            // Friendly check for missing API key to avoid 500 HTML error responses
            String apiKey = System.getenv("GEMINI_API_KEY");
            if (apiKey == null || apiKey.isBlank()) {
                return Map.of(
                        "message", message,
                        "reply",
                        "⚠️ Chưa cấu hình GEMINI_API_KEY. Vui lòng đặt biến môi trường GEMINI_API_KEY và khởi động lại ứng dụng.");
            }

            // 1) Rút trích ngữ cảnh từ CSDL (RAG đơn giản)
            String dbContext = buildDbContextFromMessage(message);

            // 2) Tạo prompt có gắn ngữ cảnh
            String promptText = buildGroundedPrompt(message, dbContext);

            ChatResponse res = chatModel.call(new Prompt(promptText));
            String output = res.getResult().getOutput().getText();
            return Map.of(
                    "message", message,
                    "reply", output,
                    "contextUsed", !dbContext.isBlank());
        } catch (Exception ex) {
            // Graceful handling, including 429 rate-limit/quota
            String err = ex.getMessage() == null ? "" : ex.getMessage();
            boolean isRateLimited = err.contains("429") || err.toLowerCase(Locale.ROOT).contains("resource exhausted");

            if (isRateLimited) {
                String dbContext = buildDbContextFromMessage(message);
                String fallback = buildDbOnlyAnswer(message, dbContext);
                return Map.of(
                        "message", message,
                        "reply",
                        "⚠️ Dịch vụ AI đang quá tải (429). Trả lời tạm dựa trên dữ liệu nội bộ:\n\n" + fallback,
                        "contextUsed", !dbContext.isBlank(),
                        "aiRateLimited", true);
            }

            // Return a graceful JSON reply (HTTP 200) so frontend can show the error nicely
            return Map.of(
                    "message", message,
                    "reply", "❌ Lỗi máy chủ khi gọi AI: " + err);
        }
    }

    // ========= Helpers: Grounding with DB =========

    private String buildDbContextFromMessage(String message) {
        if (message == null || message.isBlank())
            return "";

        // Lấy các từ khóa dài > 2 ký tự
        String[] tokens = message.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ") // giữ chữ cái, số và khoảng trắng
                .split("\\s+");

        Set<Product> hits = new LinkedHashSet<>();
        for (String t : tokens) {
            if (t.length() < 3)
                continue;
            // Tìm theo tên sản phẩm (tạm thời in-memory qua service)
            hits.addAll(productService.searchByName(t));
            if (hits.size() >= 10)
                break; // giới hạn để prompt gọn
        }

        if (hits.isEmpty())
            return "";

        NumberFormat vn = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return hits.stream().limit(10).map(p -> {
            String price = p.getPrice() != null ? vn.format(p.getPrice()) : "N/A";
            String stock = p.isInStock() ? "Còn hàng" : "Hết hàng";
            String cate = (p.getCategory() != null ? String.valueOf(p.getCategory().getName()) : "Không rõ");
            return "- [#" + p.getId() + "] " + safe(p.getName()) + " | Giá: " + price + " | Kho: " + stock
                    + " | Danh mục: " + cate;
        }).collect(Collectors.joining("\n"));
    }

    private String buildGroundedPrompt(String userMessage, String dbContext) {
        String system = "Bạn là trợ lý bán hàng của cửa hàng. Trả lời ngắn gọn, rõ ràng, bằng tiếng Việt." +
                " Nếu câu hỏi liên quan tới sản phẩm/danh mục, hãy ưu tiên dùng dữ liệu sau đây." +
                " Nếu không tìm thấy thông tin phù hợp trong dữ liệu, hãy trả lời chung chung và nêu rõ hạn chế.";

        if (dbContext == null || dbContext.isBlank()) {
            return system + "\n\nCâu hỏi: " + userMessage;
        }

        return system + "\n\nDữ liệu sản phẩm từ CSDL (tối đa 10 mục):\n" + dbContext +
                "\n\nYêu cầu: Dựa trên dữ liệu trên (nếu phù hợp), trả lời câu hỏi sau. " +
                "Nếu đề cập tới sản phẩm cụ thể, hãy kèm mã #ID.\n" +
                "Câu hỏi: " + userMessage;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private String buildDbOnlyAnswer(String userMessage, String dbContext) {
        if (dbContext == null || dbContext.isBlank()) {
            return "Hiện không có dữ liệu sản phẩm phù hợp trong hệ thống cho câu hỏi: '" + userMessage + "'.";
        }
        return "Dữ liệu liên quan trong hệ thống:\n" + dbContext +
                "\n\nGợi ý: Bạn có thể hỏi chi tiết theo mã #ID để xem thông tin cụ thể.";
    }
}
