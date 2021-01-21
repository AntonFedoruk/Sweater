<#include "security.ftlh">

<#-- messages list -->
<div class="card-columns">
    <#list messages as message>
    <div class="card my-3">
        <div>
            <#if message.filename??>
                <img class="card-img-top" src="/img/${message.filename}">
            </#if>
        </div>
        <div class="card-body">
            <h5 class="card-title">${message.tag}</h5>
            <p class="card-text">${message.text}</p>
            <div class="card-footer text-muted">
                <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                <#if message.author.id == currentUserId>
                <a href="/user-messages/${message.author.id}?message=${message.id}" class="btn btn-link ml-3">Edit</a>
                </#if>
            </div>
        </div>
    </div>
    <#else>
    No messages
    </#list>
</div>