<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>${username}</h5>
    ${message!""}
    <form method="post"> <!-- якщо не вказувати action то форма відправитьяс ноже ж адрус, звідки вона й прийшла-->
        <div class="form-group row">
            <label for="password" class="col-sm-2 col-form-label"> Password:</label>
            <div class="col-sm-6">
                <input type="password" class="form-control" id="password" name="password"
                       placeholder="Enter password"/>
            </div>
        </div>
        <div class="form-group row">
            <label for="email" class="col-sm-2 col-form-label"> Email:</label>
            <div class="col-sm-6">
                <input type="email" class="form-control" id="email" name="email"
                       placeholder="Enter email" value="${email!""}"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
</@c.page>