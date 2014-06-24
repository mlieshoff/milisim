<%@ page import="org.mili.multisim.core.plugin.Result" %>
<%@ page import="org.mili.multisim.plugins.plinga.PlingaCall" %>
<%@ page import="org.mili.multisim.plugins.plinga.PlingaParameters" %>
<%@ page import="org.mili.multisim.plugins.plinga.model.Post" %>
<%@ page import="org.mili.multisim.plugins.plinga.model.Transaction" %>
<%@ page import="org.mili.multisim.plugins.plinga.model.User" %>
<%@ page import="org.mili.multisim.plugins.plinga.servlet.PlingaServlet" %>
<%@ page import="java.util.Collection" %>
<html>

<head>
    <title>Plinga (sim)</title>
</head>

<body>

<h1>Sim-Plinga (Portal)</h1>

<%
    User user = PlingaServlet.getLoggedUser();

    PlingaCall plingaCall = new PlingaCall("listUsers");
    Result result = PlingaServlet.call(plingaCall);

    if (user != null) {
        PlingaCall userCall = new PlingaCall("getUser");
        userCall.addParameter(PlingaParameters.UID, user.getUid());
        Result userResult = PlingaServlet.call(userCall);
        user = userResult.getParameter(PlingaParameters.USER);
    }

    Collection<User> userList = result.getParameter(PlingaParameters.USER_LIST);

%>

<h1>Users</h1>
<ul>
    <%
        if (userList != null) {
            for(User listedUser : userList) {
    %>
    <li>
        <a href="<%= PlingaServlet.PLINGA_URL %>/?simaction=login&firstname=<%=listedUser.getFirstname()%>&lastname=<%=listedUser.getLastname()%>">LOGIN</a>
        <%= listedUser %></li>
    <%
            }
        }
    %>
</ul>

<% if (user != null) { %>

<h1>Wall of <%= user.getLastname() %></h1>

<ul>
    <%
        if (user.getPosts() != null) {
            for(Post post : user.getPosts()) {
    %>
    <li><%= post %></li>
    <%
            }
        }
    %>
</ul>

<h1>Transactions of <%= user.getLastname() %></h1>

<ul>
    <%
        if (user.getTransactions() != null) {
            for(Transaction transaction : user.getTransactions()) {
    %>
    <li><%= transaction %></li>
    <%
            }
        }
    %>
</ul>

<% } %>

<h1>Login</h1>

<form action="<%= PlingaServlet.PLINGA_URL %>/?simaction=login" method="post">
    <table>
        <tr>
            <td>Firstname</td>
            <td><input name="firstname" type="text" /></td>
        </tr>
        <tr>
            <td>Lastname</td>
            <td><input name="lastname" type="text" /></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" /></td>
        </tr>
    </table>
</form>

<h1>Register</h1>

<form action="<%= PlingaServlet.PLINGA_URL %>/?simaction=register" method="post">
    <table>
        <tr>
            <td>Firstname</td>
            <td><input name="firstname" type="text" /></td>
        </tr>
        <tr>
            <td>Lastname</td>
            <td><input name="lastname" type="text" /></td>
        </tr>
        <tr>
            <td>Thumbnail Url</td>
            <td><input name="thumbnailurl" type="text" /></td>
        </tr>
        <tr>
            <td>Gender</td>
            <td><input name="gender" type="text" /></td>
        </tr>
        <tr>
            <td>Locale</td>
            <td><input name="locale" type="text" /></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" /></td>
        </tr>
    </table>
</form>

</body>

</html>