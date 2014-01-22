package org.cProc.sql;

// $ANTLR 3.4 D:\\antlr_data\\CreateTest.g 2012-06-29 16:15:20

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CreateTestLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int ANDOPERATER=4;
    public static final int COMMENT=5;
    public static final int COUMER=6;
    public static final int EQUAL=7;
    public static final int ESC_SEQ=8;
    public static final int EXPONENT=9;
    public static final int FLOAT=10;
    public static final int HEX_DIGIT=11;
    public static final int ID=12;
    public static final int IN=13;
    public static final int INT=14;
    public static final int IS=15;
    public static final int LESS=16;
    public static final int LIKE=17;
    public static final int LPARENT=18;
    public static final int MORE=19;
    public static final int NOT=20;
    public static final int NOTEQUAL=21;
    public static final int NULL=22;
    public static final int NUM=23;
    public static final int OCTAL_ESC=24;
    public static final int OROPERATER=25;
    public static final int PROG=26;
    public static final int RPARENT=27;
    public static final int STAT=28;
    public static final int StringLiteral=29;
    public static final int TOK_ANDCONDITION=30;
    public static final int TOK_APP=31;
    public static final int TOK_ATOMCONDITION=32;
    public static final int TOK_COLUMLIST=33;
    public static final int TOK_COLUMN=34;
    public static final int TOK_CREATE=35;
    public static final int TOK_CREATE_TIMEINDEX=36;
    public static final int TOK_FROM=37;
    public static final int TOK_FUN=38;
    public static final int TOK_GROUP=39;
    public static final int TOK_INCONDITION=40;
    public static final int TOK_INDEX=41;
    public static final int TOK_LOAD=42;
    public static final int TOK_ORCONDITION=43;
    public static final int TOK_SELECT=44;
    public static final int TOK_SELECTLIST=45;
    public static final int UNICODE_ESC=46;
    public static final int VAR=47;
    public static final int WS=48;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public CreateTestLexer() {} 
    public CreateTestLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CreateTestLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "D:\\antlr_data\\CreateTest.g"; }

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:7:7: ( 'INT' )
            // D:\\antlr_data\\CreateTest.g:7:9: 'INT'
            {
            match("INT"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:8:7: ( 'LONG' )
            // D:\\antlr_data\\CreateTest.g:8:9: 'LONG'
            {
            match("LONG"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:9:7: ( 'STRING' )
            // D:\\antlr_data\\CreateTest.g:9:9: 'STRING'
            {
            match("STRING"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:10:7: ( 'create' )
            // D:\\antlr_data\\CreateTest.g:10:9: 'create'
            {
            match("create"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:11:7: ( 'data' )
            // D:\\antlr_data\\CreateTest.g:11:9: 'data'
            {
            match("data"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:12:7: ( 'from' )
            // D:\\antlr_data\\CreateTest.g:12:9: 'from'
            {
            match("from"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:13:7: ( 'index' )
            // D:\\antlr_data\\CreateTest.g:13:9: 'index'
            {
            match("index"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:14:7: ( 'inpath' )
            // D:\\antlr_data\\CreateTest.g:14:9: 'inpath'
            {
            match("inpath"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:15:7: ( 'into' )
            // D:\\antlr_data\\CreateTest.g:15:9: 'into'
            {
            match("into"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:16:7: ( 'load' )
            // D:\\antlr_data\\CreateTest.g:16:9: 'load'
            {
            match("load"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:17:7: ( 'local' )
            // D:\\antlr_data\\CreateTest.g:17:9: 'local'
            {
            match("local"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:18:7: ( 'of' )
            // D:\\antlr_data\\CreateTest.g:18:9: 'of'
            {
            match("of"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:19:7: ( 'on' )
            // D:\\antlr_data\\CreateTest.g:19:9: 'on'
            {
            match("on"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:20:7: ( 'select' )
            // D:\\antlr_data\\CreateTest.g:20:9: 'select'
            {
            match("select"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:21:7: ( 'table' )
            // D:\\antlr_data\\CreateTest.g:21:9: 'table'
            {
            match("table"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:22:7: ( 'time' )
            // D:\\antlr_data\\CreateTest.g:22:9: 'time'
            {
            match("time"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:23:7: ( 'where' )
            // D:\\antlr_data\\CreateTest.g:23:9: 'where'
            {
            match("where"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:112:7: ( '==' )
            // D:\\antlr_data\\CreateTest.g:112:9: '=='
            {
            match("=="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "NOTEQUAL"
    public final void mNOTEQUAL() throws RecognitionException {
        try {
            int _type = NOTEQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:114:9: ( '<>' )
            // D:\\antlr_data\\CreateTest.g:114:12: '<>'
            {
            match("<>"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOTEQUAL"

    // $ANTLR start "LESS"
    public final void mLESS() throws RecognitionException {
        try {
            int _type = LESS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:116:6: ( '<=' )
            // D:\\antlr_data\\CreateTest.g:116:10: '<='
            {
            match("<="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LESS"

    // $ANTLR start "MORE"
    public final void mMORE() throws RecognitionException {
        try {
            int _type = MORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:118:6: ( '>=' )
            // D:\\antlr_data\\CreateTest.g:118:10: '>='
            {
            match(">="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "MORE"

    // $ANTLR start "IN"
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:120:4: ( 'in' )
            // D:\\antlr_data\\CreateTest.g:120:7: 'in'
            {
            match("in"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IN"

    // $ANTLR start "IS"
    public final void mIS() throws RecognitionException {
        try {
            int _type = IS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:122:4: ( 'is' )
            // D:\\antlr_data\\CreateTest.g:122:6: 'is'
            {
            match("is"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IS"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:124:5: ( 'not' )
            // D:\\antlr_data\\CreateTest.g:124:7: 'not'
            {
            match("not"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:126:6: ( 'null' )
            // D:\\antlr_data\\CreateTest.g:126:8: 'null'
            {
            match("null"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "LIKE"
    public final void mLIKE() throws RecognitionException {
        try {
            int _type = LIKE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:128:6: ( 'like' )
            // D:\\antlr_data\\CreateTest.g:128:8: 'like'
            {
            match("like"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LIKE"

    // $ANTLR start "OROPERATER"
    public final void mOROPERATER() throws RecognitionException {
        try {
            int _type = OROPERATER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:133:2: ( 'or' )
            // D:\\antlr_data\\CreateTest.g:133:4: 'or'
            {
            match("or"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OROPERATER"

    // $ANTLR start "ANDOPERATER"
    public final void mANDOPERATER() throws RecognitionException {
        try {
            int _type = ANDOPERATER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:136:2: ( 'and' )
            // D:\\antlr_data\\CreateTest.g:136:4: 'and'
            {
            match("and"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ANDOPERATER"

    // $ANTLR start "StringLiteral"
    public final void mStringLiteral() throws RecognitionException {
        try {
            int _type = StringLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:139:5: ( ( '\\'' (~ ( '\\'' | '\\\\' ) | ( '\\\\' . ) )* '\\'' | '\\\"' (~ ( '\\\"' | '\\\\' ) | ( '\\\\' . ) )* '\\\"' )+ )
            // D:\\antlr_data\\CreateTest.g:140:5: ( '\\'' (~ ( '\\'' | '\\\\' ) | ( '\\\\' . ) )* '\\'' | '\\\"' (~ ( '\\\"' | '\\\\' ) | ( '\\\\' . ) )* '\\\"' )+
            {
            // D:\\antlr_data\\CreateTest.g:140:5: ( '\\'' (~ ( '\\'' | '\\\\' ) | ( '\\\\' . ) )* '\\'' | '\\\"' (~ ( '\\\"' | '\\\\' ) | ( '\\\\' . ) )* '\\\"' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\'') ) {
                    alt3=1;
                }
                else if ( (LA3_0=='\"') ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:140:7: '\\'' (~ ( '\\'' | '\\\\' ) | ( '\\\\' . ) )* '\\''
            	    {
            	    match('\''); 

            	    // D:\\antlr_data\\CreateTest.g:140:12: (~ ( '\\'' | '\\\\' ) | ( '\\\\' . ) )*
            	    loop1:
            	    do {
            	        int alt1=3;
            	        int LA1_0 = input.LA(1);

            	        if ( ((LA1_0 >= '\u0000' && LA1_0 <= '&')||(LA1_0 >= '(' && LA1_0 <= '[')||(LA1_0 >= ']' && LA1_0 <= '\uFFFF')) ) {
            	            alt1=1;
            	        }
            	        else if ( (LA1_0=='\\') ) {
            	            alt1=2;
            	        }


            	        switch (alt1) {
            	    	case 1 :
            	    	    // D:\\antlr_data\\CreateTest.g:140:14: ~ ( '\\'' | '\\\\' )
            	    	    {
            	    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	    	        input.consume();
            	    	    }
            	    	    else {
            	    	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	    	        recover(mse);
            	    	        throw mse;
            	    	    }


            	    	    }
            	    	    break;
            	    	case 2 :
            	    	    // D:\\antlr_data\\CreateTest.g:140:29: ( '\\\\' . )
            	    	    {
            	    	    // D:\\antlr_data\\CreateTest.g:140:29: ( '\\\\' . )
            	    	    // D:\\antlr_data\\CreateTest.g:140:30: '\\\\' .
            	    	    {
            	    	    match('\\'); 

            	    	    matchAny(); 

            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop1;
            	        }
            	    } while (true);


            	    match('\''); 

            	    }
            	    break;
            	case 2 :
            	    // D:\\antlr_data\\CreateTest.g:141:7: '\\\"' (~ ( '\\\"' | '\\\\' ) | ( '\\\\' . ) )* '\\\"'
            	    {
            	    match('\"'); 

            	    // D:\\antlr_data\\CreateTest.g:141:12: (~ ( '\\\"' | '\\\\' ) | ( '\\\\' . ) )*
            	    loop2:
            	    do {
            	        int alt2=3;
            	        int LA2_0 = input.LA(1);

            	        if ( ((LA2_0 >= '\u0000' && LA2_0 <= '!')||(LA2_0 >= '#' && LA2_0 <= '[')||(LA2_0 >= ']' && LA2_0 <= '\uFFFF')) ) {
            	            alt2=1;
            	        }
            	        else if ( (LA2_0=='\\') ) {
            	            alt2=2;
            	        }


            	        switch (alt2) {
            	    	case 1 :
            	    	    // D:\\antlr_data\\CreateTest.g:141:14: ~ ( '\\\"' | '\\\\' )
            	    	    {
            	    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	    	        input.consume();
            	    	    }
            	    	    else {
            	    	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	    	        recover(mse);
            	    	        throw mse;
            	    	    }


            	    	    }
            	    	    break;
            	    	case 2 :
            	    	    // D:\\antlr_data\\CreateTest.g:141:29: ( '\\\\' . )
            	    	    {
            	    	    // D:\\antlr_data\\CreateTest.g:141:29: ( '\\\\' . )
            	    	    // D:\\antlr_data\\CreateTest.g:141:30: '\\\\' .
            	    	    {
            	    	    match('\\'); 

            	    	    matchAny(); 

            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop2;
            	        }
            	    } while (true);


            	    match('\"'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "StringLiteral"

    // $ANTLR start "COUMER"
    public final void mCOUMER() throws RecognitionException {
        try {
            int _type = COUMER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:145:9: ( ',' )
            // D:\\antlr_data\\CreateTest.g:145:11: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COUMER"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:147:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // D:\\antlr_data\\CreateTest.g:147:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // D:\\antlr_data\\CreateTest.g:147:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'Z')||LA4_0=='_'||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:150:5: ( ( '-' )? ( '0' .. '9' )+ )
            // D:\\antlr_data\\CreateTest.g:150:7: ( '-' )? ( '0' .. '9' )+
            {
            // D:\\antlr_data\\CreateTest.g:150:7: ( '-' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='-') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:150:8: '-'
                    {
                    match('-'); 

                    }
                    break;

            }


            // D:\\antlr_data\\CreateTest.g:150:13: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "LPARENT"
    public final void mLPARENT() throws RecognitionException {
        try {
            int _type = LPARENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:152:9: ( '(' )
            // D:\\antlr_data\\CreateTest.g:152:11: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LPARENT"

    // $ANTLR start "RPARENT"
    public final void mRPARENT() throws RecognitionException {
        try {
            int _type = RPARENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:154:9: ( ')' )
            // D:\\antlr_data\\CreateTest.g:154:11: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RPARENT"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:157:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
            int alt13=3;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:157:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
                    {
                    // D:\\antlr_data\\CreateTest.g:157:9: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    match('.'); 

                    // D:\\antlr_data\\CreateTest.g:157:25: ( '0' .. '9' )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    // D:\\antlr_data\\CreateTest.g:157:37: ( EXPONENT )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0=='E'||LA9_0=='e') ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // D:\\antlr_data\\CreateTest.g:157:37: EXPONENT
                            {
                            mEXPONENT(); 


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:158:9: '.' ( '0' .. '9' )+ ( EXPONENT )?
                    {
                    match('.'); 

                    // D:\\antlr_data\\CreateTest.g:158:13: ( '0' .. '9' )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);


                    // D:\\antlr_data\\CreateTest.g:158:25: ( EXPONENT )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='E'||LA11_0=='e') ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // D:\\antlr_data\\CreateTest.g:158:25: EXPONENT
                            {
                            mEXPONENT(); 


                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // D:\\antlr_data\\CreateTest.g:159:9: ( '0' .. '9' )+ EXPONENT
                    {
                    // D:\\antlr_data\\CreateTest.g:159:9: ( '0' .. '9' )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);


                    mEXPONENT(); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:163:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0=='/') ) {
                int LA17_1 = input.LA(2);

                if ( (LA17_1=='/') ) {
                    alt17=1;
                }
                else if ( (LA17_1=='*') ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }
            switch (alt17) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:163:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("//"); 



                    // D:\\antlr_data\\CreateTest.g:163:14: (~ ( '\\n' | '\\r' ) )*
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\t')||(LA14_0 >= '\u000B' && LA14_0 <= '\f')||(LA14_0 >= '\u000E' && LA14_0 <= '\uFFFF')) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop14;
                        }
                    } while (true);


                    // D:\\antlr_data\\CreateTest.g:163:28: ( '\\r' )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0=='\r') ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // D:\\antlr_data\\CreateTest.g:163:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }


                    match('\n'); 

                    _channel=HIDDEN;

                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:164:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 



                    // D:\\antlr_data\\CreateTest.g:164:14: ( options {greedy=false; } : . )*
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( (LA16_0=='*') ) {
                            int LA16_1 = input.LA(2);

                            if ( (LA16_1=='/') ) {
                                alt16=2;
                            }
                            else if ( ((LA16_1 >= '\u0000' && LA16_1 <= '.')||(LA16_1 >= '0' && LA16_1 <= '\uFFFF')) ) {
                                alt16=1;
                            }


                        }
                        else if ( ((LA16_0 >= '\u0000' && LA16_0 <= ')')||(LA16_0 >= '+' && LA16_0 <= '\uFFFF')) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:164:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop16;
                        }
                    } while (true);


                    match("*/"); 



                    _channel=HIDDEN;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\antlr_data\\CreateTest.g:167:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // D:\\antlr_data\\CreateTest.g:167:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "EXPONENT"
    public final void mEXPONENT() throws RecognitionException {
        try {
            // D:\\antlr_data\\CreateTest.g:182:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // D:\\antlr_data\\CreateTest.g:182:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // D:\\antlr_data\\CreateTest.g:182:22: ( '+' | '-' )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='+'||LA18_0=='-') ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // D:\\antlr_data\\CreateTest.g:182:33: ( '0' .. '9' )+
            int cnt19=0;
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0 >= '0' && LA19_0 <= '9')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt19 >= 1 ) break loop19;
                        EarlyExitException eee =
                            new EarlyExitException(19, input);
                        throw eee;
                }
                cnt19++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EXPONENT"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // D:\\antlr_data\\CreateTest.g:185:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // D:\\antlr_data\\CreateTest.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // D:\\antlr_data\\CreateTest.g:189:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt20=3;
            int LA20_0 = input.LA(1);

            if ( (LA20_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt20=1;
                    }
                    break;
                case 'u':
                    {
                    alt20=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt20=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }
            switch (alt20) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:189:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 

                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:190:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // D:\\antlr_data\\CreateTest.g:191:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // D:\\antlr_data\\CreateTest.g:196:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt21=3;
            int LA21_0 = input.LA(1);

            if ( (LA21_0=='\\') ) {
                int LA21_1 = input.LA(2);

                if ( ((LA21_1 >= '0' && LA21_1 <= '3')) ) {
                    int LA21_2 = input.LA(3);

                    if ( ((LA21_2 >= '0' && LA21_2 <= '7')) ) {
                        int LA21_4 = input.LA(4);

                        if ( ((LA21_4 >= '0' && LA21_4 <= '7')) ) {
                            alt21=1;
                        }
                        else {
                            alt21=2;
                        }
                    }
                    else {
                        alt21=3;
                    }
                }
                else if ( ((LA21_1 >= '4' && LA21_1 <= '7')) ) {
                    int LA21_3 = input.LA(3);

                    if ( ((LA21_3 >= '0' && LA21_3 <= '7')) ) {
                        alt21=2;
                    }
                    else {
                        alt21=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }
            switch (alt21) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:196:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:197:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 3 :
                    // D:\\antlr_data\\CreateTest.g:198:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // D:\\antlr_data\\CreateTest.g:203:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // D:\\antlr_data\\CreateTest.g:203:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 

            match('u'); 

            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // D:\\antlr_data\\CreateTest.g:1:8: ( T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | EQUAL | NOTEQUAL | LESS | MORE | IN | IS | NOT | NULL | LIKE | OROPERATER | ANDOPERATER | StringLiteral | COUMER | ID | INT | LPARENT | RPARENT | FLOAT | COMMENT | WS )
        int alt22=37;
        alt22 = dfa22.predict(input);
        switch (alt22) {
            case 1 :
                // D:\\antlr_data\\CreateTest.g:1:10: T__49
                {
                mT__49(); 


                }
                break;
            case 2 :
                // D:\\antlr_data\\CreateTest.g:1:16: T__50
                {
                mT__50(); 


                }
                break;
            case 3 :
                // D:\\antlr_data\\CreateTest.g:1:22: T__51
                {
                mT__51(); 


                }
                break;
            case 4 :
                // D:\\antlr_data\\CreateTest.g:1:28: T__52
                {
                mT__52(); 


                }
                break;
            case 5 :
                // D:\\antlr_data\\CreateTest.g:1:34: T__53
                {
                mT__53(); 


                }
                break;
            case 6 :
                // D:\\antlr_data\\CreateTest.g:1:40: T__54
                {
                mT__54(); 


                }
                break;
            case 7 :
                // D:\\antlr_data\\CreateTest.g:1:46: T__55
                {
                mT__55(); 


                }
                break;
            case 8 :
                // D:\\antlr_data\\CreateTest.g:1:52: T__56
                {
                mT__56(); 


                }
                break;
            case 9 :
                // D:\\antlr_data\\CreateTest.g:1:58: T__57
                {
                mT__57(); 


                }
                break;
            case 10 :
                // D:\\antlr_data\\CreateTest.g:1:64: T__58
                {
                mT__58(); 


                }
                break;
            case 11 :
                // D:\\antlr_data\\CreateTest.g:1:70: T__59
                {
                mT__59(); 


                }
                break;
            case 12 :
                // D:\\antlr_data\\CreateTest.g:1:76: T__60
                {
                mT__60(); 


                }
                break;
            case 13 :
                // D:\\antlr_data\\CreateTest.g:1:82: T__61
                {
                mT__61(); 


                }
                break;
            case 14 :
                // D:\\antlr_data\\CreateTest.g:1:88: T__62
                {
                mT__62(); 


                }
                break;
            case 15 :
                // D:\\antlr_data\\CreateTest.g:1:94: T__63
                {
                mT__63(); 


                }
                break;
            case 16 :
                // D:\\antlr_data\\CreateTest.g:1:100: T__64
                {
                mT__64(); 


                }
                break;
            case 17 :
                // D:\\antlr_data\\CreateTest.g:1:106: T__65
                {
                mT__65(); 


                }
                break;
            case 18 :
                // D:\\antlr_data\\CreateTest.g:1:112: EQUAL
                {
                mEQUAL(); 


                }
                break;
            case 19 :
                // D:\\antlr_data\\CreateTest.g:1:118: NOTEQUAL
                {
                mNOTEQUAL(); 


                }
                break;
            case 20 :
                // D:\\antlr_data\\CreateTest.g:1:127: LESS
                {
                mLESS(); 


                }
                break;
            case 21 :
                // D:\\antlr_data\\CreateTest.g:1:132: MORE
                {
                mMORE(); 


                }
                break;
            case 22 :
                // D:\\antlr_data\\CreateTest.g:1:137: IN
                {
                mIN(); 


                }
                break;
            case 23 :
                // D:\\antlr_data\\CreateTest.g:1:140: IS
                {
                mIS(); 


                }
                break;
            case 24 :
                // D:\\antlr_data\\CreateTest.g:1:143: NOT
                {
                mNOT(); 


                }
                break;
            case 25 :
                // D:\\antlr_data\\CreateTest.g:1:147: NULL
                {
                mNULL(); 


                }
                break;
            case 26 :
                // D:\\antlr_data\\CreateTest.g:1:152: LIKE
                {
                mLIKE(); 


                }
                break;
            case 27 :
                // D:\\antlr_data\\CreateTest.g:1:157: OROPERATER
                {
                mOROPERATER(); 


                }
                break;
            case 28 :
                // D:\\antlr_data\\CreateTest.g:1:168: ANDOPERATER
                {
                mANDOPERATER(); 


                }
                break;
            case 29 :
                // D:\\antlr_data\\CreateTest.g:1:180: StringLiteral
                {
                mStringLiteral(); 


                }
                break;
            case 30 :
                // D:\\antlr_data\\CreateTest.g:1:194: COUMER
                {
                mCOUMER(); 


                }
                break;
            case 31 :
                // D:\\antlr_data\\CreateTest.g:1:201: ID
                {
                mID(); 


                }
                break;
            case 32 :
                // D:\\antlr_data\\CreateTest.g:1:204: INT
                {
                mINT(); 


                }
                break;
            case 33 :
                // D:\\antlr_data\\CreateTest.g:1:208: LPARENT
                {
                mLPARENT(); 


                }
                break;
            case 34 :
                // D:\\antlr_data\\CreateTest.g:1:216: RPARENT
                {
                mRPARENT(); 


                }
                break;
            case 35 :
                // D:\\antlr_data\\CreateTest.g:1:224: FLOAT
                {
                mFLOAT(); 


                }
                break;
            case 36 :
                // D:\\antlr_data\\CreateTest.g:1:230: COMMENT
                {
                mCOMMENT(); 


                }
                break;
            case 37 :
                // D:\\antlr_data\\CreateTest.g:1:238: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    protected DFA22 dfa22 = new DFA22(this);
    static final String DFA13_eotS =
        "\5\uffff";
    static final String DFA13_eofS =
        "\5\uffff";
    static final String DFA13_minS =
        "\2\56\3\uffff";
    static final String DFA13_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA13_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA13_specialS =
        "\5\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
            "",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "156:1: FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
        }
    }
    static final String DFA22_eotS =
        "\1\uffff\14\24\3\uffff\2\24\4\uffff\1\25\5\uffff\6\24\1\73\1\74"+
        "\2\24\1\100\1\101\1\102\4\24\2\uffff\3\24\1\112\10\24\2\uffff\3"+
        "\24\3\uffff\4\24\1\132\1\24\1\134\1\uffff\1\135\2\24\1\140\1\141"+
        "\2\24\1\144\1\145\1\24\1\147\2\24\1\152\1\24\1\uffff\1\154\2\uffff"+
        "\2\24\2\uffff\1\157\1\24\2\uffff\1\161\1\uffff\1\24\1\163\1\uffff"+
        "\1\164\1\uffff\1\165\1\166\1\uffff\1\167\1\uffff\1\170\6\uffff";
    static final String DFA22_eofS =
        "\171\uffff";
    static final String DFA22_minS =
        "\1\11\1\116\1\117\1\124\1\162\1\141\1\162\1\156\1\151\1\146\1\145"+
        "\1\141\1\150\1\uffff\1\75\1\uffff\1\157\1\156\4\uffff\1\56\5\uffff"+
        "\1\124\1\116\1\122\1\145\1\164\1\157\2\60\1\141\1\153\3\60\1\154"+
        "\1\142\1\155\1\145\2\uffff\1\164\1\154\1\144\1\60\1\107\1\111\2"+
        "\141\1\155\1\145\1\141\1\157\2\uffff\1\144\1\141\1\145\3\uffff\1"+
        "\145\1\154\1\145\1\162\1\60\1\154\1\60\1\uffff\1\60\1\116\1\164"+
        "\2\60\1\170\1\164\2\60\1\154\1\60\1\143\1\145\1\60\1\145\1\uffff"+
        "\1\60\2\uffff\1\107\1\145\2\uffff\1\60\1\150\2\uffff\1\60\1\uffff"+
        "\1\164\1\60\1\uffff\1\60\1\uffff\2\60\1\uffff\1\60\1\uffff\1\60"+
        "\6\uffff";
    static final String DFA22_maxS =
        "\1\172\1\116\1\117\1\124\1\162\1\141\1\162\1\163\1\157\1\162\1\145"+
        "\1\151\1\150\1\uffff\1\76\1\uffff\1\165\1\156\4\uffff\1\145\5\uffff"+
        "\1\124\1\116\1\122\1\145\1\164\1\157\2\172\1\143\1\153\3\172\1\154"+
        "\1\142\1\155\1\145\2\uffff\1\164\1\154\1\144\1\172\1\107\1\111\2"+
        "\141\1\155\1\145\1\141\1\157\2\uffff\1\144\1\141\1\145\3\uffff\1"+
        "\145\1\154\1\145\1\162\1\172\1\154\1\172\1\uffff\1\172\1\116\1\164"+
        "\2\172\1\170\1\164\2\172\1\154\1\172\1\143\1\145\1\172\1\145\1\uffff"+
        "\1\172\2\uffff\1\107\1\145\2\uffff\1\172\1\150\2\uffff\1\172\1\uffff"+
        "\1\164\1\172\1\uffff\1\172\1\uffff\2\172\1\uffff\1\172\1\uffff\1"+
        "\172\6\uffff";
    static final String DFA22_acceptS =
        "\15\uffff\1\22\1\uffff\1\25\2\uffff\1\35\1\36\1\37\1\40\1\uffff"+
        "\1\41\1\42\1\43\1\44\1\45\21\uffff\1\23\1\24\14\uffff\1\26\1\27"+
        "\3\uffff\1\14\1\15\1\33\7\uffff\1\1\17\uffff\1\30\1\uffff\1\34\1"+
        "\2\2\uffff\1\5\1\6\2\uffff\1\11\1\12\1\uffff\1\32\2\uffff\1\20\1"+
        "\uffff\1\31\2\uffff\1\7\1\uffff\1\13\1\uffff\1\17\1\21\1\3\1\4\1"+
        "\10\1\16";
    static final String DFA22_specialS =
        "\171\uffff}>";
    static final String[] DFA22_transitionS = {
            "\2\33\2\uffff\1\33\22\uffff\1\33\1\uffff\1\22\4\uffff\1\22\1"+
            "\27\1\30\2\uffff\1\23\1\25\1\31\1\32\12\26\2\uffff\1\16\1\15"+
            "\1\17\2\uffff\10\24\1\1\2\24\1\2\6\24\1\3\7\24\4\uffff\1\24"+
            "\1\uffff\1\21\1\24\1\4\1\5\1\24\1\6\2\24\1\7\2\24\1\10\1\24"+
            "\1\20\1\11\3\24\1\12\1\13\2\24\1\14\3\24",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42\4\uffff\1\43",
            "\1\45\5\uffff\1\44",
            "\1\46\7\uffff\1\47\3\uffff\1\50",
            "\1\51",
            "\1\52\7\uffff\1\53",
            "\1\54",
            "",
            "\1\56\1\55",
            "",
            "\1\57\5\uffff\1\60",
            "\1\61",
            "",
            "",
            "",
            "",
            "\1\31\1\uffff\12\26\13\uffff\1\31\37\uffff\1\31",
            "",
            "",
            "",
            "",
            "",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\3\24\1\70\13\24\1"+
            "\71\3\24\1\72\6\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\75\1\uffff\1\76",
            "\1\77",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "",
            "",
            "\1\107",
            "\1\110",
            "\1\111",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "",
            "",
            "\1\123",
            "\1\124",
            "\1\125",
            "",
            "",
            "",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\133",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\136",
            "\1\137",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\142",
            "\1\143",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\146",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\150",
            "\1\151",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\153",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "",
            "\1\155",
            "\1\156",
            "",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\1\160",
            "",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "\1\162",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | EQUAL | NOTEQUAL | LESS | MORE | IN | IS | NOT | NULL | LIKE | OROPERATER | ANDOPERATER | StringLiteral | COUMER | ID | INT | LPARENT | RPARENT | FLOAT | COMMENT | WS );";
        }
    }
 

}