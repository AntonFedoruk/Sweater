<#import "parts/common.ftlh" as c>

<@c.page>
<#-- search form-->
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/messages" class="form-inline">
                <input type="text" class="form-control" name="filter" value="${filter!}" placeholder="Search by tag" />
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>
    <#include "parts/messageEdit.ftlh" />
    <#include  "parts/messageList.ftlh" />
</@c.page>