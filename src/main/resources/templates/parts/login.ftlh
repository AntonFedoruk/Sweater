<#macro login path isRegisterForm>
    <form method="post" action="${path}">
        <div class="form-group row">
            <label for="username" class="col-sm-2 col-form-label"> User name:</label>
            <div class="col-sm-6">
                <input type="text" class="form-control" id="username" name="username"
                       placeholder="Enter username"/>
            </div>
        </div>
        <div class="form-group row">
            <label for="password" class="col-sm-2 col-form-label"> Password:</label>
            <div class="col-sm-6">
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="Enter password"/>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label for="email" class="col-sm-2 col-form-label"> Email:</label>
                <div class="col-sm-6">
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="Enter email"/>
                </div>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
        <button type="submit" class="btn btn-primary"><#if isRegisterForm>Create<#else>Sign In</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Sign Out</button>
    </form>
</#macro>