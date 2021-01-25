<#-- message adding -->
<a class="btn btn-primary" data-toggle="collapse" href="#addNewMessage" role="button" aria-expanded="false"
   aria-controls="addNewMessage">
    Message editor
</a>
<div class="collapse <#if message??>show</#if>" id="addNewMessage">
    <div class="form-group mt-3">
        <form method="post" action="/messages" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control <#if (errorsMap['textError'])??>is-invalid</#if>"
                       value="<#if message??>${message.text}</#if>" name="text" placeholder="type message" />
                <#if (errorsMap['textError'])??>
                    <div class="invalid-feedbackk">
                        ${errorsMap['textError']}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control <#if (errorsMap['tagError'])??>is-invalid</#if>"
                       value="<#if message??>${message.tag}</#if>" name="tag" placeholder="type tag" />
                <#if (errorsMap['tagError'])??>
                    <div class="invalid-feedbackk">
                        ${errorsMap['tagError']}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <#--                        <label class="custom-file-label" for="customFile">Choose file</label>-->
                    <input type="file" name="file" id="customFile" value="message.filename" placeholder="choose file" />
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>" />
            <button type="submit" class="btn btn-primary">Save message</button>
        </form>
    </div>
</div>