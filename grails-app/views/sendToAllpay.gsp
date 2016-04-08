%{-- GrailsAllpayPlugin --}%
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <script language="javascript">
    window.onload = function () {
      document.getElementById("cathayForm").submit();
    }
  </script>
</head>
<body>
<form method="POST" id="cathayForm" action="${url}">
  <g:each in="${valueObj}" var="k" status="i">
    <input type="hidden" name="${k.key}" value="${k.value}"/>
  </g:each>
</form>
</body>
</html>