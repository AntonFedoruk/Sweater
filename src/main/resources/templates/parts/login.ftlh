<#macro login path isRegisterForm>
    <form method="post" action="${path}" xmlns="http://www.w3.org/1999/html">
        <div class="form-group row">
            <label for="username" class="col-sm-2 col-form-label">User name:</label>
            <div class="col-sm-6">
                <input type="text" class="form-control <#if usernameError??>is-invalid</#if>"
                       id="username" name="username" placeholder="Enter username" value="<#if user??>${user.username}</#if>" />
                <#if usernameError??>
                    <div class="invalid-feedbackk">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label for="password" class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" class="form-control <#if passwordError??>is-invalid</#if>"
                       id="password" name="password" placeholder="Enter password" />
                <#if passwordError??>
                    <div class="invalid-feedbackk">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label for="password2" class="col-sm-2 col-form-label"> Password confirmation:</label>
                <div class="col-sm-6">
                    <input type="password" class="form-control <#if password2Error??>is-invalid</#if>"
                           id="password2" name="password2" placeholder="Retype password" />
                    <#if password2Error??>
                        <div class="invalid-feedbackk">
                            ${password2Error}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label for="email" class="col-sm-2 col-form-label"> Email:</label>
                <div class="col-sm-6">
                    <input type="email" class="form-control <#if emailError??>is-invalid</#if>" id="email" name="email"
                           placeholder="Enter email" value="<#if user??>${user.email}</#if>" />
                    <#if emailError??>
                        <div class="invalid-feedbackk">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="g-recaptcha" data-sitekey="6Le8sDQaAAAAAK-jrkjhsT5yirQqriWoAm3zMI1c"></div>
                <#if captchaError??>
                    <div class="alert alert-danger" role="alert">
                        ${captchaError}
                    </div>
                </#if>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
        <button type="submit" class="btn btn-primary"><#if isRegisterForm>Create<#else>Sign In</#if></button>
    </form>
</#macro>

<#macro logout>
<#--    <#include "security.ftlh" />-->
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button type="submit" class="btn btn-primary"><#if user??>Sign Out<#else>Log in</#if></button>
    </form>
</#macro>