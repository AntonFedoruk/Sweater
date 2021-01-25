delete
from messages;

insert into messages(id, tag, text, user_id)
values (1, 'my-tag', 'first message', 1),
       (2, 'more', 'second message', 1),
       (3, 'my-tag', 'third message', 1),
       (4, 'tag-tag-tag', 'fourth message', 1);

update hibernate_sequence set next_val=10;