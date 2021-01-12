<#import "parts/common.ftlh" as c>

<@c.page>
<#-- search form-->
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/messages" class="form-inline">
                <input type="text" class="form-control" name="filter" value="${filter!}" placeholder="Search by tag"/>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>
<#-- message adding -->
    <a class="btn btn-primary" data-toggle="collapse" href="#addNewMessage" role="button" aria-expanded="false"
       aria-controls="addNewMessage">
        Add new message
    </a>
    <div class="collapse" id="addNewMessage">
        <div class="form-group mt-3">
            <form method="post" action="/messages" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="text" class="form-control" name="text" placeholder="type message"/>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="tag" placeholder="type tag"/>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <label class="custom-file-label" for="customFile">Choose file</label>
                        <input type="file" name="file" id="customFile">
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary">Add message</button>
            </form>
        </div>
    </div>
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
                        ${message.authorName}
                        <a href="#" class="btn btn-link ml-3">Edit</a>
                    </div>

                </div>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>