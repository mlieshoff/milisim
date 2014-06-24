<%@ page import="org.mili.multisim.plugins.plinga.model.User" %>
<%@ page import="org.mili.multisim.plugins.plinga.servlet.PlingaServlet" %>
<html>

<head>
    <title>Plinga (sim)</title>

    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>

    <script type="text/javascript">
        var api = {
            post: function () {
                jQuery.ajax({
                    async: false,
                    type: "POST",
                    url: '<%=PlingaServlet.PLINGA_URL%>/?simaction=post'
                            + '&target=<%= request.getParameter("target") %>'
                            + '&title=<%= request.getParameter("title") %>'
                            + '&body=<%= request.getParameter("body") %>'
                            + '&pic=<%= request.getParameter("pic") %>'
                            + '&linktext=<%= request.getParameter("linktext") %>'
                            + '&params=<%= request.getParameter("params") %>'
                });
            }
        };

        function receiveMessage(event)
        {
            // event.source is window.opener
            // event.data is "hello there!"

            // Assuming you've verified the origin of the received message (which
            // you must do in any case), a convenient idiom for replying to a
            // message is to call postMessage on event.source and provide
            // event.origin as the targetOrigin.
            event.source.postMessage("hi there yourself!  the secret response " + "is: rheeeeet!", event.origin);
        }

        window.addEventListener("message", receiveMessage, false);

    </script>

</head>

<body>

<h1>Sim-Plinga (Post)</h1>

<%
    User user = PlingaServlet.getLoggedUser();
%>

<form id="myform">
    <input type="button" value="Send" onclick="api.post()" />
</form>

</body>

</html>