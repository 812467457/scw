<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}/"/>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function () {
                let array = [1, 5, 6];
                let requestBody = JSON.stringify(array);
                $.ajax({
                    "url": "send/array.html",
                    "type": "post",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "text",
                    "success": function (response) {
                        console.log(response);
                    },
                    "error": function (response) {
                        console.log(response);
                    }
                });
            });

            $("#btn2").click(function () {
                layer.msg("layer弹框");
            });
        });

    </script>
</head>

<body>
<a href="test/ssm.html">aaaa</a>
<br>
<button id="btn1">test</button>
<button id="btn2">layer</button>
</body>
</html>
