<#import "parts/common.ftlh" as c>
<#import  "parts/login.ftlh" as l>

<@c.page>
    <div>
        <@l.logout/>
    </div>
    <div>
        <form method="post" action="/messages">
            <label> Text:
                <input type="text" name="text" placeholder="type message"/>
            </label>
            <label> Tag:
                <input type="text" name="tag" placeholder="type tag"/>
            </label>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit">Add message</button>
        </form>
    </div>
    <br/>
    <div>
        <form method="get" action="/messages">
            <label>Filter by tag:
                <input type="text" name="filter" value="${filter!}"/>
            </label>
            <button type="submit">Find</button>
        </form>
    </div>

    <p>Messages list</p>
    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
        </div>
    <#else>
        No messages
    </#list>
</@c.page>