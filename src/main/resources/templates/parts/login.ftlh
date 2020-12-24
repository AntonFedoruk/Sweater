<#macro login path>
    <form class="form-signin" method="post" action="${path}">
        <p>
            <label for="username"> User Name:
                <input type="text" id="username" name="username" placeholder="Username"/>
            </label>
        </p>
        <p>
            <label for="password"> Password:
                <input type="password" id="password" name="password" placeholder="Password"/>
            </label>
        </p>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit">Sign In</button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="submit" value="Sign Out"/>
    </form>
</#macro>