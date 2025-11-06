document.addEventListener('DOMContentLoaded', function () {
    const sendBtn = document.getElementById('send-btn');
    const chatInput = document.getElementById('message-input');
    const chatWindow = document.getElementById('chat-window');

    // main.js
    const apiUrl = '/api/ai/chat';


    function addMessage(text, isUser) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${isUser ? 'user' : 'ai'}`;
        
        const textSpan = document.createElement('span');
        textSpan.className = 'text';
        textSpan.textContent = text;
        
        messageDiv.appendChild(textSpan);
        chatWindow.appendChild(messageDiv);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    function showLoading() {
        const loadingDiv = document.createElement('div');
        loadingDiv.className = 'message ai';
        loadingDiv.innerHTML = '<span class="text">⏳ Đang chờ...</span>';
        chatWindow.appendChild(loadingDiv);
        chatWindow.scrollTop = chatWindow.scrollHeight;
        return loadingDiv;
    }

    function sendMessage() {
        const message = chatInput.value.trim();
        if (!message) return;

        addMessage(message, true);
        chatInput.value = '';

        const loading = showLoading();

        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ message })
        })
        .then(async (res) => {
            if (!res.ok) throw new Error('HTTP ' + res.status);
            return res.json();
        })
        .then(data => {
            loading.remove();
            addMessage(data.reply, false);
        })
        .catch(err => {
            loading.remove();
            addMessage('❌ Lỗi: ' + err.message, false);
        });
    }

    sendBtn.addEventListener('click', sendMessage);
    
    chatInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            sendMessage();
        }
    });
});
