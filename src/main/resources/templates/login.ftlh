<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div class="container">
        <h2>Login</h2>
        <@l.login "/login"/>
        <a href="/registration">Add new user</a>
    </div>
</@c.page>