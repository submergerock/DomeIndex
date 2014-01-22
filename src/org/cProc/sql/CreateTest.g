grammar CreateTest;
options
{
output=AST;
ASTLabelType=CommonTree;
language=Java;
}

tokens{
	PROG; STAT; NUM; VAR;TOK_CREATE;TOK_COLUMLIST;TOK_COLUMN;TOK_LOAD;TOK_CREATE_TIMEINDEX;TOK_ORCONDITION;TOK_ANDCONDITION;TOK_ATOMCONDITION;TOK_SELECT;TOK_SELECTLIST;TOK_GROUP;TOK_INCONDITION;TOK_FROM;TOK_APP;TOK_INDEX;TOK_FUN;
}

condition
	:   orcondition	
	;
orcondition
	:   andcondtion (OROPERATER andcondtion )* -> ^(TOK_ORCONDITION andcondtion* )
		
	;
andcondtion
	:   conditionItem (ANDOPERATER  andcondtion)* ->^(TOK_ANDCONDITION conditionItem  andcondtion* )
	    |	LPARENT orcondition   RPARENT  (ANDOPERATER  andcondtion)*->^(TOK_ANDCONDITION orcondition  andcondtion*)
	    ;	    
conditionItem
	:
	conditonName MORE INT	 -> ^(TOK_ATOMCONDITION  conditonName MORE INT )
	| 
	conditonName EQUAL allItem 	 -> ^(TOK_ATOMCONDITION conditonName  EQUAL allItem )
	| 
	conditonName NOTEQUAL allItem	 -> ^(TOK_ATOMCONDITION conditonName NOTEQUAL allItem )
	| 
	conditonName LESS INT  	 -> ^(TOK_ATOMCONDITION conditonName  LESS INT )
	|
	conditonName NOT? IN  LPARENT INT(COUMER INT)* RPARENT ->^(TOK_ATOMCONDITION  conditonName  IN NOT? INT*  )
	|
	conditonName  NOT? IN  LPARENT StringLiteral(COUMER StringLiteral)* RPARENT ->^(TOK_ATOMCONDITION conditonName  IN NOT? StringLiteral* )
	|
	conditonName IS NOT? NULL ->^(TOK_ATOMCONDITION conditonName IS NOT?  )
	|
	conditonName LIKE StringLiteral	 -> ^(TOK_ATOMCONDITION  conditonName LIKE StringLiteral )
	;
fun	:
	ID LPARENT  (parma (COUMER parma)* )? RPARENT -> ^(TOK_FUN ID (parma (parma)* )?) 
	;
conditonName:	
	ID|fun 
	;
parma	:	
	ID |INT	
	;
allItem	:	INT | StringLiteral 
	;

statement
	:	loadDataStateMent  | createTableStateMent | createIndexStateMent | selectStateMent
	;
	

selectStateMent
	:	'select'  selectList 'from' tableName 'on' appName  ('where' condition)?  -> ^(TOK_SELECT tableName appName selectList  condition?  )
	;
selectList
	:	 ID (COUMER ID)* -> ^(TOK_SELECTLIST ID*)
	;	

loadDataStateMent
	:	'load' 'data' host 'inpath' path 'into' 'table' tableName 'on' appName -> ^(TOK_LOAD appName tableName path)
	;


createTableStateMent : 'create' 'table' tableName LPARENT columnList  RPARENT 'on' appName -> ^(TOK_CREATE appName  tableName  columnList)
;

createIndexStateMent 
	:	createTimeIndexStateMent
	;
createTimeIndexStateMent
	:	'create' 'time' 'index' indexName 'on' tableName 'of' appName LPARENT timeIndexCloumnList  RPARENT -> ^(TOK_CREATE_TIMEINDEX appName tableName indexName timeIndexCloumnList)
	;	

columnList:		column (COUMER! column)* 
	;
	
column	:	 ID type -> ^(TOK_COLUMN ID type)
	;
	
type    :	'LONG'|'INT'|('STRING' INT);

timeIndexCloumnList
	:	timeIndexcolumn (COUMER! timeIndexOthercolumn)* 
	;
timeIndexcolumn	: timeIndexCloumnType  ID type -> ^(TOK_COLUMN ID type timeIndexCloumnType)
	;	
timeIndexOthercolumn	:  ID type -> ^(TOK_COLUMN ID type )
	;	
timeIndexCloumnType
	:	'time'
	;

appName	:	ID->^(TOK_APP ID);

tableName:	ID ->^(TOK_FROM ID);

indexName
	:	ID ->^(TOK_INDEX ID);

host	:	'local' 
	;
path    :	StringLiteral
	;

EQUAL	: '=='	
	;
NOTEQUAL:  '<>'	
	;
LESS	:   '<='
	;	
MORE	:   '>='
	;
IN	:  'in'
	;
IS	: 'is'
	;
NOT	: 'not'
	;
NULL	: 'null'
	;
LIKE	: 'like'
	;	
	
	
OROPERATER
	: 'or'
	;
ANDOPERATER
	: 'and'
	;	
StringLiteral    
    :
    ( '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '\"' ( ~('\"'|'\\') | ('\\' .) )* '\"'
    )+
    ;	

COUMER  :	',' ;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	('-')?'0'..'9'+
    ;
LPARENT	:	'('
	;
RPARENT :	')'
	;
FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;
//STRING
//    :  '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\''
//    ;

//CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
//    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
