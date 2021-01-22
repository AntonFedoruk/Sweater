<#import "parts/common.ftlh" as c>

<@c.page>
    <h3>${userChannel.username}</h3>
    <div>${type}</div>
    <div class="list-group">
        <#list users as user>
            <a href="/user-messages/${user.id}" class="list-group-item list-group-item-action">${user.getUsername()}</a>
        </#list>
    </div>
</@c.page>