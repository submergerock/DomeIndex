grammar Learning;


se: e '%'
| e '!'
;
e : '(' e ')'
| ID
;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;