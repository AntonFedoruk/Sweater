<#include "security.ftlh" />
<#import "pager.ftlh" as p>

<#-- messages list -->
<@p.pager url page /><#-- url and page get from controller -->
<div class="card-columns" id="message-list">
    <#list page.content as message>
        <div class="card my-3" data-id="${message.id}">
            <#if message.filename??>
                <div>
                    <img class="card-img-top" src="/img/${message.filename}"/>
                </div>
            </#if>
            <div class="card-body">
                <h5 class="card-title">${message.tag}</h5>
                <p class="card-text">${message.text}</p>
                <div class="card-footer text-muted container">
                    <div class="row">
                        <a class="col align-self-center"
                           href="/user-messages/${message.author.id}">${message.authorName}</a>
                        <a class="col align-self-center" href="/messages/${message.id}/like">
                            <#if message.meLiked>
                                <i class="fas fa-heart"></i>
                            <#else>
                                <i class="far fa-heart"></i>
                            </#if>
                            ${message.likes}
                        </a>
                        <#if message.author.id == currentUserId>
                            <a href="/user-messages/${message.author.id}?message=${message.id}"
                               class="col btn btn-link ml-3">Edit</a>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    <#else>
        No messages
    </#list>
</div>
<@p.pager url page />