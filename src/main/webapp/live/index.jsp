<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Live - Vergaderlokalen</title>
    <link rel="stylesheet" type="text/css" href="common.css">
    <link rel="stylesheet" type="text/css" href="live/main.css">
</head>
<body>
<%@include file="../navigation.jspf"%>
<main>
    <div id="timeline"></div>
    <canvas id="blueprint" width="1741" height="350"></canvas>
</main>
<footer>

</footer>

<script type="text/javascript">${rooms}</script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript" src="timeline.js"></script>
<script type="text/javascript" src="live/main.js"></script>
<script type="text/javascript" src="common.js"></script>
</body>
</html>
