/**
 * 公共 JavaScript 函数
 */

// 消息自动消失功能（2秒后淡出）
(function() {
    document.addEventListener('DOMContentLoaded', function() {
        // 处理所有类型的消息提示
        const messages = document.querySelectorAll('.message, .error-message, .success-message');
        
        messages.forEach(function(message) {
            // 2秒后开始淡出
            setTimeout(function() {
                message.classList.add('fade-out');
                // 淡出动画结束后移除元素
                setTimeout(function() {
                    message.remove();
                }, 300); // 淡出动画持续时间
            }, 2000); // 2秒后开始淡出
        });
    });
})();
