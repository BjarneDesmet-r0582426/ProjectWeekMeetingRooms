<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Vergaderlokalen</title>
    <link rel="stylesheet" type="text/css" href="common.css">
    <link rel="stylesheet" type="text/css" href="app/main.css">
    <script>
        if(!('fetch' in window)) {
            var promisePolyfill = document.createElement('script');
            var fetchPolyfill = document.createElement('script');
            promisePolyfill.src = 'https://cdn.jsdelivr.net/npm/promise-polyfill@6/dist/promise.min.js';
            fetchPolyfill.src = 'https://cdnjs.cloudflare.com/ajax/libs/fetch/2.0.3/fetch.min.js';
            document.head.appendChild(promisePolyfill);
            document.head.appendChild(fetchPolyfill);
        }
    </script>
</head>
<body>
<%@ include file="../navigation.jspf" %>
<main>
    <div id="timeline-controls">
        <a href="#" id="timeline-back">&lt;</a>
        <span id="timeline-date"></span>
        <a href="#" id="timeline-forward">&gt;</a>
    </div>
    <div id="timeline"></div>
    <canvas id="blueprint" width="1741" height="350"></canvas>
</main>
<footer>

</footer>

<script type="text/javascript">${rooms}</script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript" src="timeline.js"></script>
<script type="text/javascript" src="app/main.js"></script>
<script type="text/javascript" src="common.js"></script>
</body>
</html>
