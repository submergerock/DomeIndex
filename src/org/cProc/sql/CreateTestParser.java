package org.cProc.sql;

// $ANTLR 3.4 D:\\antlr_data\\CreateTest.g 2012-06-29 16:15:19

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class CreateTestParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ANDOPERATER", "COMMENT", "COUMER", "EQUAL", "ESC_SEQ", "EXPONENT", "FLOAT", "HEX_DIGIT", "ID", "IN", "INT", "IS", "LESS", "LIKE", "LPARENT", "MORE", "NOT", "NOTEQUAL", "NULL", "NUM", "OCTAL_ESC", "OROPERATER", "PROG", "RPARENT", "STAT", "StringLiteral", "TOK_ANDCONDITION", "TOK_APP", "TOK_ATOMCONDITION", "TOK_COLUMLIST", "TOK_COLUMN", "TOK_CREATE", "TOK_CREATE_TIMEINDEX", "TOK_FROM", "TOK_FUN", "TOK_GROUP", "TOK_INCONDITION", "TOK_INDEX", "TOK_LOAD", "TOK_ORCONDITION", "TOK_SELECT", "TOK_SELECTLIST", "UNICODE_ESC", "VAR", "WS", "'INT'", "'LONG'", "'STRING'", "'create'", "'data'", "'from'", "'index'", "'inpath'", "'into'", "'load'", "'local'", "'of'", "'on'", "'select'", "'table'", "'time'", "'where'"
    };

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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public CreateTestParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public CreateTestParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return CreateTestParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\antlr_data\\CreateTest.g"; }


    public static class condition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "condition"
    // D:\\antlr_data\\CreateTest.g:13:1: condition : orcondition ;
    public final CreateTestParser.condition_return condition() throws RecognitionException {
        CreateTestParser.condition_return retval = new CreateTestParser.condition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        CreateTestParser.orcondition_return orcondition1 =null;



        try {
            // D:\\antlr_data\\CreateTest.g:14:2: ( orcondition )
            // D:\\antlr_data\\CreateTest.g:14:6: orcondition
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_orcondition_in_condition82);
            orcondition1=orcondition();

            state._fsp--;

            adaptor.addChild(root_0, orcondition1.getTree());

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "condition"


    public static class orcondition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "orcondition"
    // D:\\antlr_data\\CreateTest.g:16:1: orcondition : andcondtion ( OROPERATER andcondtion )* -> ^( TOK_ORCONDITION ( andcondtion )* ) ;
    public final CreateTestParser.orcondition_return orcondition() throws RecognitionException {
        CreateTestParser.orcondition_return retval = new CreateTestParser.orcondition_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token OROPERATER3=null;
        CreateTestParser.andcondtion_return andcondtion2 =null;

        CreateTestParser.andcondtion_return andcondtion4 =null;


        CommonTree OROPERATER3_tree=null;
        RewriteRuleTokenStream stream_OROPERATER=new RewriteRuleTokenStream(adaptor,"token OROPERATER");
        RewriteRuleSubtreeStream stream_andcondtion=new RewriteRuleSubtreeStream(adaptor,"rule andcondtion");
        try {
            // D:\\antlr_data\\CreateTest.g:17:2: ( andcondtion ( OROPERATER andcondtion )* -> ^( TOK_ORCONDITION ( andcondtion )* ) )
            // D:\\antlr_data\\CreateTest.g:17:6: andcondtion ( OROPERATER andcondtion )*
            {
            pushFollow(FOLLOW_andcondtion_in_orcondition95);
            andcondtion2=andcondtion();

            state._fsp--;

            stream_andcondtion.add(andcondtion2.getTree());

            // D:\\antlr_data\\CreateTest.g:17:18: ( OROPERATER andcondtion )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==OROPERATER) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:17:19: OROPERATER andcondtion
            	    {
            	    OROPERATER3=(Token)match(input,OROPERATER,FOLLOW_OROPERATER_in_orcondition98);  
            	    stream_OROPERATER.add(OROPERATER3);


            	    pushFollow(FOLLOW_andcondtion_in_orcondition100);
            	    andcondtion4=andcondtion();

            	    state._fsp--;

            	    stream_andcondtion.add(andcondtion4.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            // AST REWRITE
            // elements: andcondtion
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 17:45: -> ^( TOK_ORCONDITION ( andcondtion )* )
            {
                // D:\\antlr_data\\CreateTest.g:17:48: ^( TOK_ORCONDITION ( andcondtion )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_ORCONDITION, "TOK_ORCONDITION")
                , root_1);

                // D:\\antlr_data\\CreateTest.g:17:66: ( andcondtion )*
                while ( stream_andcondtion.hasNext() ) {
                    adaptor.addChild(root_1, stream_andcondtion.nextTree());

                }
                stream_andcondtion.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "orcondition"


    public static class andcondtion_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "andcondtion"
    // D:\\antlr_data\\CreateTest.g:20:1: andcondtion : ( conditionItem ( ANDOPERATER andcondtion )* -> ^( TOK_ANDCONDITION conditionItem ( andcondtion )* ) | LPARENT orcondition RPARENT ( ANDOPERATER andcondtion )* -> ^( TOK_ANDCONDITION orcondition ( andcondtion )* ) );
    public final CreateTestParser.andcondtion_return andcondtion() throws RecognitionException {
        CreateTestParser.andcondtion_return retval = new CreateTestParser.andcondtion_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ANDOPERATER6=null;
        Token LPARENT8=null;
        Token RPARENT10=null;
        Token ANDOPERATER11=null;
        CreateTestParser.conditionItem_return conditionItem5 =null;

        CreateTestParser.andcondtion_return andcondtion7 =null;

        CreateTestParser.orcondition_return orcondition9 =null;

        CreateTestParser.andcondtion_return andcondtion12 =null;


        CommonTree ANDOPERATER6_tree=null;
        CommonTree LPARENT8_tree=null;
        CommonTree RPARENT10_tree=null;
        CommonTree ANDOPERATER11_tree=null;
        RewriteRuleTokenStream stream_LPARENT=new RewriteRuleTokenStream(adaptor,"token LPARENT");
        RewriteRuleTokenStream stream_ANDOPERATER=new RewriteRuleTokenStream(adaptor,"token ANDOPERATER");
        RewriteRuleTokenStream stream_RPARENT=new RewriteRuleTokenStream(adaptor,"token RPARENT");
        RewriteRuleSubtreeStream stream_andcondtion=new RewriteRuleSubtreeStream(adaptor,"rule andcondtion");
        RewriteRuleSubtreeStream stream_orcondition=new RewriteRuleSubtreeStream(adaptor,"rule orcondition");
        RewriteRuleSubtreeStream stream_conditionItem=new RewriteRuleSubtreeStream(adaptor,"rule conditionItem");
        try {
            // D:\\antlr_data\\CreateTest.g:21:2: ( conditionItem ( ANDOPERATER andcondtion )* -> ^( TOK_ANDCONDITION conditionItem ( andcondtion )* ) | LPARENT orcondition RPARENT ( ANDOPERATER andcondtion )* -> ^( TOK_ANDCONDITION orcondition ( andcondtion )* ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ID) ) {
                alt4=1;
            }
            else if ( (LA4_0==LPARENT) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:21:6: conditionItem ( ANDOPERATER andcondtion )*
                    {
                    pushFollow(FOLLOW_conditionItem_in_andcondtion128);
                    conditionItem5=conditionItem();

                    state._fsp--;

                    stream_conditionItem.add(conditionItem5.getTree());

                    // D:\\antlr_data\\CreateTest.g:21:20: ( ANDOPERATER andcondtion )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==ANDOPERATER) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:21:21: ANDOPERATER andcondtion
                    	    {
                    	    ANDOPERATER6=(Token)match(input,ANDOPERATER,FOLLOW_ANDOPERATER_in_andcondtion131);  
                    	    stream_ANDOPERATER.add(ANDOPERATER6);


                    	    pushFollow(FOLLOW_andcondtion_in_andcondtion134);
                    	    andcondtion7=andcondtion();

                    	    state._fsp--;

                    	    stream_andcondtion.add(andcondtion7.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: andcondtion, conditionItem
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 21:48: -> ^( TOK_ANDCONDITION conditionItem ( andcondtion )* )
                    {
                        // D:\\antlr_data\\CreateTest.g:21:50: ^( TOK_ANDCONDITION conditionItem ( andcondtion )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ANDCONDITION, "TOK_ANDCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditionItem.nextTree());

                        // D:\\antlr_data\\CreateTest.g:21:84: ( andcondtion )*
                        while ( stream_andcondtion.hasNext() ) {
                            adaptor.addChild(root_1, stream_andcondtion.nextTree());

                        }
                        stream_andcondtion.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:22:8: LPARENT orcondition RPARENT ( ANDOPERATER andcondtion )*
                    {
                    LPARENT8=(Token)match(input,LPARENT,FOLLOW_LPARENT_in_andcondtion157);  
                    stream_LPARENT.add(LPARENT8);


                    pushFollow(FOLLOW_orcondition_in_andcondtion159);
                    orcondition9=orcondition();

                    state._fsp--;

                    stream_orcondition.add(orcondition9.getTree());

                    RPARENT10=(Token)match(input,RPARENT,FOLLOW_RPARENT_in_andcondtion163);  
                    stream_RPARENT.add(RPARENT10);


                    // D:\\antlr_data\\CreateTest.g:22:39: ( ANDOPERATER andcondtion )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( (LA3_0==ANDOPERATER) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:22:40: ANDOPERATER andcondtion
                    	    {
                    	    ANDOPERATER11=(Token)match(input,ANDOPERATER,FOLLOW_ANDOPERATER_in_andcondtion167);  
                    	    stream_ANDOPERATER.add(ANDOPERATER11);


                    	    pushFollow(FOLLOW_andcondtion_in_andcondtion170);
                    	    andcondtion12=andcondtion();

                    	    state._fsp--;

                    	    stream_andcondtion.add(andcondtion12.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: andcondtion, orcondition
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 22:66: -> ^( TOK_ANDCONDITION orcondition ( andcondtion )* )
                    {
                        // D:\\antlr_data\\CreateTest.g:22:68: ^( TOK_ANDCONDITION orcondition ( andcondtion )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ANDCONDITION, "TOK_ANDCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_orcondition.nextTree());

                        // D:\\antlr_data\\CreateTest.g:22:100: ( andcondtion )*
                        while ( stream_andcondtion.hasNext() ) {
                            adaptor.addChild(root_1, stream_andcondtion.nextTree());

                        }
                        stream_andcondtion.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "andcondtion"


    public static class conditionItem_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditionItem"
    // D:\\antlr_data\\CreateTest.g:24:1: conditionItem : ( conditonName MORE INT -> ^( TOK_ATOMCONDITION conditonName MORE INT ) | conditonName EQUAL allItem -> ^( TOK_ATOMCONDITION conditonName EQUAL allItem ) | conditonName NOTEQUAL allItem -> ^( TOK_ATOMCONDITION conditonName NOTEQUAL allItem ) | conditonName LESS INT -> ^( TOK_ATOMCONDITION conditonName LESS INT ) | conditonName ( NOT )? IN LPARENT INT ( COUMER INT )* RPARENT -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( INT )* ) | conditonName ( NOT )? IN LPARENT StringLiteral ( COUMER StringLiteral )* RPARENT -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( StringLiteral )* ) | conditonName IS ( NOT )? NULL -> ^( TOK_ATOMCONDITION conditonName IS ( NOT )? ) | conditonName LIKE StringLiteral -> ^( TOK_ATOMCONDITION conditonName LIKE StringLiteral ) );
    public final CreateTestParser.conditionItem_return conditionItem() throws RecognitionException {
        CreateTestParser.conditionItem_return retval = new CreateTestParser.conditionItem_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token MORE14=null;
        Token INT15=null;
        Token EQUAL17=null;
        Token NOTEQUAL20=null;
        Token LESS23=null;
        Token INT24=null;
        Token NOT26=null;
        Token IN27=null;
        Token LPARENT28=null;
        Token INT29=null;
        Token COUMER30=null;
        Token INT31=null;
        Token RPARENT32=null;
        Token NOT34=null;
        Token IN35=null;
        Token LPARENT36=null;
        Token StringLiteral37=null;
        Token COUMER38=null;
        Token StringLiteral39=null;
        Token RPARENT40=null;
        Token IS42=null;
        Token NOT43=null;
        Token NULL44=null;
        Token LIKE46=null;
        Token StringLiteral47=null;
        CreateTestParser.conditonName_return conditonName13 =null;

        CreateTestParser.conditonName_return conditonName16 =null;

        CreateTestParser.allItem_return allItem18 =null;

        CreateTestParser.conditonName_return conditonName19 =null;

        CreateTestParser.allItem_return allItem21 =null;

        CreateTestParser.conditonName_return conditonName22 =null;

        CreateTestParser.conditonName_return conditonName25 =null;

        CreateTestParser.conditonName_return conditonName33 =null;

        CreateTestParser.conditonName_return conditonName41 =null;

        CreateTestParser.conditonName_return conditonName45 =null;


        CommonTree MORE14_tree=null;
        CommonTree INT15_tree=null;
        CommonTree EQUAL17_tree=null;
        CommonTree NOTEQUAL20_tree=null;
        CommonTree LESS23_tree=null;
        CommonTree INT24_tree=null;
        CommonTree NOT26_tree=null;
        CommonTree IN27_tree=null;
        CommonTree LPARENT28_tree=null;
        CommonTree INT29_tree=null;
        CommonTree COUMER30_tree=null;
        CommonTree INT31_tree=null;
        CommonTree RPARENT32_tree=null;
        CommonTree NOT34_tree=null;
        CommonTree IN35_tree=null;
        CommonTree LPARENT36_tree=null;
        CommonTree StringLiteral37_tree=null;
        CommonTree COUMER38_tree=null;
        CommonTree StringLiteral39_tree=null;
        CommonTree RPARENT40_tree=null;
        CommonTree IS42_tree=null;
        CommonTree NOT43_tree=null;
        CommonTree NULL44_tree=null;
        CommonTree LIKE46_tree=null;
        CommonTree StringLiteral47_tree=null;
        RewriteRuleTokenStream stream_StringLiteral=new RewriteRuleTokenStream(adaptor,"token StringLiteral");
        RewriteRuleTokenStream stream_RPARENT=new RewriteRuleTokenStream(adaptor,"token RPARENT");
        RewriteRuleTokenStream stream_IN=new RewriteRuleTokenStream(adaptor,"token IN");
        RewriteRuleTokenStream stream_MORE=new RewriteRuleTokenStream(adaptor,"token MORE");
        RewriteRuleTokenStream stream_IS=new RewriteRuleTokenStream(adaptor,"token IS");
        RewriteRuleTokenStream stream_EQUAL=new RewriteRuleTokenStream(adaptor,"token EQUAL");
        RewriteRuleTokenStream stream_NULL=new RewriteRuleTokenStream(adaptor,"token NULL");
        RewriteRuleTokenStream stream_LESS=new RewriteRuleTokenStream(adaptor,"token LESS");
        RewriteRuleTokenStream stream_LPARENT=new RewriteRuleTokenStream(adaptor,"token LPARENT");
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
        RewriteRuleTokenStream stream_COUMER=new RewriteRuleTokenStream(adaptor,"token COUMER");
        RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
        RewriteRuleTokenStream stream_LIKE=new RewriteRuleTokenStream(adaptor,"token LIKE");
        RewriteRuleTokenStream stream_NOTEQUAL=new RewriteRuleTokenStream(adaptor,"token NOTEQUAL");
        RewriteRuleSubtreeStream stream_conditonName=new RewriteRuleSubtreeStream(adaptor,"rule conditonName");
        RewriteRuleSubtreeStream stream_allItem=new RewriteRuleSubtreeStream(adaptor,"rule allItem");
        try {
            // D:\\antlr_data\\CreateTest.g:25:2: ( conditonName MORE INT -> ^( TOK_ATOMCONDITION conditonName MORE INT ) | conditonName EQUAL allItem -> ^( TOK_ATOMCONDITION conditonName EQUAL allItem ) | conditonName NOTEQUAL allItem -> ^( TOK_ATOMCONDITION conditonName NOTEQUAL allItem ) | conditonName LESS INT -> ^( TOK_ATOMCONDITION conditonName LESS INT ) | conditonName ( NOT )? IN LPARENT INT ( COUMER INT )* RPARENT -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( INT )* ) | conditonName ( NOT )? IN LPARENT StringLiteral ( COUMER StringLiteral )* RPARENT -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( StringLiteral )* ) | conditonName IS ( NOT )? NULL -> ^( TOK_ATOMCONDITION conditonName IS ( NOT )? ) | conditonName LIKE StringLiteral -> ^( TOK_ATOMCONDITION conditonName LIKE StringLiteral ) )
            int alt10=8;
            alt10 = dfa10.predict(input);
            switch (alt10) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:26:2: conditonName MORE INT
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem202);
                    conditonName13=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName13.getTree());

                    MORE14=(Token)match(input,MORE,FOLLOW_MORE_in_conditionItem204);  
                    stream_MORE.add(MORE14);


                    INT15=(Token)match(input,INT,FOLLOW_INT_in_conditionItem206);  
                    stream_INT.add(INT15);


                    // AST REWRITE
                    // elements: INT, MORE, conditonName
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 26:25: -> ^( TOK_ATOMCONDITION conditonName MORE INT )
                    {
                        // D:\\antlr_data\\CreateTest.g:26:28: ^( TOK_ATOMCONDITION conditonName MORE INT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_MORE.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_INT.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:28:2: conditonName EQUAL allItem
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem228);
                    conditonName16=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName16.getTree());

                    EQUAL17=(Token)match(input,EQUAL,FOLLOW_EQUAL_in_conditionItem230);  
                    stream_EQUAL.add(EQUAL17);


                    pushFollow(FOLLOW_allItem_in_conditionItem232);
                    allItem18=allItem();

                    state._fsp--;

                    stream_allItem.add(allItem18.getTree());

                    // AST REWRITE
                    // elements: EQUAL, conditonName, allItem
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 28:31: -> ^( TOK_ATOMCONDITION conditonName EQUAL allItem )
                    {
                        // D:\\antlr_data\\CreateTest.g:28:34: ^( TOK_ATOMCONDITION conditonName EQUAL allItem )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_EQUAL.nextNode()
                        );

                        adaptor.addChild(root_1, stream_allItem.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 3 :
                    // D:\\antlr_data\\CreateTest.g:30:2: conditonName NOTEQUAL allItem
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem255);
                    conditonName19=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName19.getTree());

                    NOTEQUAL20=(Token)match(input,NOTEQUAL,FOLLOW_NOTEQUAL_in_conditionItem257);  
                    stream_NOTEQUAL.add(NOTEQUAL20);


                    pushFollow(FOLLOW_allItem_in_conditionItem259);
                    allItem21=allItem();

                    state._fsp--;

                    stream_allItem.add(allItem21.getTree());

                    // AST REWRITE
                    // elements: allItem, NOTEQUAL, conditonName
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 30:33: -> ^( TOK_ATOMCONDITION conditonName NOTEQUAL allItem )
                    {
                        // D:\\antlr_data\\CreateTest.g:30:36: ^( TOK_ATOMCONDITION conditonName NOTEQUAL allItem )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_NOTEQUAL.nextNode()
                        );

                        adaptor.addChild(root_1, stream_allItem.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 4 :
                    // D:\\antlr_data\\CreateTest.g:32:2: conditonName LESS INT
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem280);
                    conditonName22=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName22.getTree());

                    LESS23=(Token)match(input,LESS,FOLLOW_LESS_in_conditionItem282);  
                    stream_LESS.add(LESS23);


                    INT24=(Token)match(input,INT,FOLLOW_INT_in_conditionItem284);  
                    stream_INT.add(INT24);


                    // AST REWRITE
                    // elements: INT, conditonName, LESS
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 32:27: -> ^( TOK_ATOMCONDITION conditonName LESS INT )
                    {
                        // D:\\antlr_data\\CreateTest.g:32:30: ^( TOK_ATOMCONDITION conditonName LESS INT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_LESS.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_INT.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 5 :
                    // D:\\antlr_data\\CreateTest.g:34:2: conditonName ( NOT )? IN LPARENT INT ( COUMER INT )* RPARENT
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem307);
                    conditonName25=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName25.getTree());

                    // D:\\antlr_data\\CreateTest.g:34:15: ( NOT )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==NOT) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // D:\\antlr_data\\CreateTest.g:34:15: NOT
                            {
                            NOT26=(Token)match(input,NOT,FOLLOW_NOT_in_conditionItem309);  
                            stream_NOT.add(NOT26);


                            }
                            break;

                    }


                    IN27=(Token)match(input,IN,FOLLOW_IN_in_conditionItem312);  
                    stream_IN.add(IN27);


                    LPARENT28=(Token)match(input,LPARENT,FOLLOW_LPARENT_in_conditionItem315);  
                    stream_LPARENT.add(LPARENT28);


                    INT29=(Token)match(input,INT,FOLLOW_INT_in_conditionItem317);  
                    stream_INT.add(INT29);


                    // D:\\antlr_data\\CreateTest.g:34:35: ( COUMER INT )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==COUMER) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:34:36: COUMER INT
                    	    {
                    	    COUMER30=(Token)match(input,COUMER,FOLLOW_COUMER_in_conditionItem319);  
                    	    stream_COUMER.add(COUMER30);


                    	    INT31=(Token)match(input,INT,FOLLOW_INT_in_conditionItem321);  
                    	    stream_INT.add(INT31);


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    RPARENT32=(Token)match(input,RPARENT,FOLLOW_RPARENT_in_conditionItem325);  
                    stream_RPARENT.add(RPARENT32);


                    // AST REWRITE
                    // elements: IN, INT, conditonName, NOT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 34:57: -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( INT )* )
                    {
                        // D:\\antlr_data\\CreateTest.g:34:59: ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( INT )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_IN.nextNode()
                        );

                        // D:\\antlr_data\\CreateTest.g:34:97: ( NOT )?
                        if ( stream_NOT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOT.nextNode()
                            );

                        }
                        stream_NOT.reset();

                        // D:\\antlr_data\\CreateTest.g:34:102: ( INT )*
                        while ( stream_INT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_INT.nextNode()
                            );

                        }
                        stream_INT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 6 :
                    // D:\\antlr_data\\CreateTest.g:36:2: conditonName ( NOT )? IN LPARENT StringLiteral ( COUMER StringLiteral )* RPARENT
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem350);
                    conditonName33=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName33.getTree());

                    // D:\\antlr_data\\CreateTest.g:36:16: ( NOT )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==NOT) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // D:\\antlr_data\\CreateTest.g:36:16: NOT
                            {
                            NOT34=(Token)match(input,NOT,FOLLOW_NOT_in_conditionItem353);  
                            stream_NOT.add(NOT34);


                            }
                            break;

                    }


                    IN35=(Token)match(input,IN,FOLLOW_IN_in_conditionItem356);  
                    stream_IN.add(IN35);


                    LPARENT36=(Token)match(input,LPARENT,FOLLOW_LPARENT_in_conditionItem359);  
                    stream_LPARENT.add(LPARENT36);


                    StringLiteral37=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_conditionItem361);  
                    stream_StringLiteral.add(StringLiteral37);


                    // D:\\antlr_data\\CreateTest.g:36:46: ( COUMER StringLiteral )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==COUMER) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:36:47: COUMER StringLiteral
                    	    {
                    	    COUMER38=(Token)match(input,COUMER,FOLLOW_COUMER_in_conditionItem363);  
                    	    stream_COUMER.add(COUMER38);


                    	    StringLiteral39=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_conditionItem365);  
                    	    stream_StringLiteral.add(StringLiteral39);


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    RPARENT40=(Token)match(input,RPARENT,FOLLOW_RPARENT_in_conditionItem369);  
                    stream_RPARENT.add(RPARENT40);


                    // AST REWRITE
                    // elements: StringLiteral, conditonName, IN, NOT
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 36:78: -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( StringLiteral )* )
                    {
                        // D:\\antlr_data\\CreateTest.g:36:80: ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( StringLiteral )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_IN.nextNode()
                        );

                        // D:\\antlr_data\\CreateTest.g:36:117: ( NOT )?
                        if ( stream_NOT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOT.nextNode()
                            );

                        }
                        stream_NOT.reset();

                        // D:\\antlr_data\\CreateTest.g:36:122: ( StringLiteral )*
                        while ( stream_StringLiteral.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_StringLiteral.nextNode()
                            );

                        }
                        stream_StringLiteral.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 7 :
                    // D:\\antlr_data\\CreateTest.g:38:2: conditonName IS ( NOT )? NULL
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem392);
                    conditonName41=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName41.getTree());

                    IS42=(Token)match(input,IS,FOLLOW_IS_in_conditionItem394);  
                    stream_IS.add(IS42);


                    // D:\\antlr_data\\CreateTest.g:38:18: ( NOT )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==NOT) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // D:\\antlr_data\\CreateTest.g:38:18: NOT
                            {
                            NOT43=(Token)match(input,NOT,FOLLOW_NOT_in_conditionItem396);  
                            stream_NOT.add(NOT43);


                            }
                            break;

                    }


                    NULL44=(Token)match(input,NULL,FOLLOW_NULL_in_conditionItem399);  
                    stream_NULL.add(NULL44);


                    // AST REWRITE
                    // elements: NOT, conditonName, IS
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 38:28: -> ^( TOK_ATOMCONDITION conditonName IS ( NOT )? )
                    {
                        // D:\\antlr_data\\CreateTest.g:38:30: ^( TOK_ATOMCONDITION conditonName IS ( NOT )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_IS.nextNode()
                        );

                        // D:\\antlr_data\\CreateTest.g:38:66: ( NOT )?
                        if ( stream_NOT.hasNext() ) {
                            adaptor.addChild(root_1, 
                            stream_NOT.nextNode()
                            );

                        }
                        stream_NOT.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 8 :
                    // D:\\antlr_data\\CreateTest.g:40:2: conditonName LIKE StringLiteral
                    {
                    pushFollow(FOLLOW_conditonName_in_conditionItem419);
                    conditonName45=conditonName();

                    state._fsp--;

                    stream_conditonName.add(conditonName45.getTree());

                    LIKE46=(Token)match(input,LIKE,FOLLOW_LIKE_in_conditionItem421);  
                    stream_LIKE.add(LIKE46);


                    StringLiteral47=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_conditionItem423);  
                    stream_StringLiteral.add(StringLiteral47);


                    // AST REWRITE
                    // elements: conditonName, StringLiteral, LIKE
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 40:35: -> ^( TOK_ATOMCONDITION conditonName LIKE StringLiteral )
                    {
                        // D:\\antlr_data\\CreateTest.g:40:38: ^( TOK_ATOMCONDITION conditonName LIKE StringLiteral )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TOK_ATOMCONDITION, "TOK_ATOMCONDITION")
                        , root_1);

                        adaptor.addChild(root_1, stream_conditonName.nextTree());

                        adaptor.addChild(root_1, 
                        stream_LIKE.nextNode()
                        );

                        adaptor.addChild(root_1, 
                        stream_StringLiteral.nextNode()
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "conditionItem"


    public static class fun_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "fun"
    // D:\\antlr_data\\CreateTest.g:42:1: fun : ID LPARENT ( parma ( COUMER parma )* )? RPARENT -> ^( TOK_FUN ID ( parma ( parma )* )? ) ;
    public final CreateTestParser.fun_return fun() throws RecognitionException {
        CreateTestParser.fun_return retval = new CreateTestParser.fun_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID48=null;
        Token LPARENT49=null;
        Token COUMER51=null;
        Token RPARENT53=null;
        CreateTestParser.parma_return parma50 =null;

        CreateTestParser.parma_return parma52 =null;


        CommonTree ID48_tree=null;
        CommonTree LPARENT49_tree=null;
        CommonTree COUMER51_tree=null;
        CommonTree RPARENT53_tree=null;
        RewriteRuleTokenStream stream_LPARENT=new RewriteRuleTokenStream(adaptor,"token LPARENT");
        RewriteRuleTokenStream stream_COUMER=new RewriteRuleTokenStream(adaptor,"token COUMER");
        RewriteRuleTokenStream stream_RPARENT=new RewriteRuleTokenStream(adaptor,"token RPARENT");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_parma=new RewriteRuleSubtreeStream(adaptor,"rule parma");
        try {
            // D:\\antlr_data\\CreateTest.g:42:5: ( ID LPARENT ( parma ( COUMER parma )* )? RPARENT -> ^( TOK_FUN ID ( parma ( parma )* )? ) )
            // D:\\antlr_data\\CreateTest.g:43:2: ID LPARENT ( parma ( COUMER parma )* )? RPARENT
            {
            ID48=(Token)match(input,ID,FOLLOW_ID_in_fun448);  
            stream_ID.add(ID48);


            LPARENT49=(Token)match(input,LPARENT,FOLLOW_LPARENT_in_fun450);  
            stream_LPARENT.add(LPARENT49);


            // D:\\antlr_data\\CreateTest.g:43:14: ( parma ( COUMER parma )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ID||LA12_0==INT) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:43:15: parma ( COUMER parma )*
                    {
                    pushFollow(FOLLOW_parma_in_fun454);
                    parma50=parma();

                    state._fsp--;

                    stream_parma.add(parma50.getTree());

                    // D:\\antlr_data\\CreateTest.g:43:21: ( COUMER parma )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==COUMER) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // D:\\antlr_data\\CreateTest.g:43:22: COUMER parma
                    	    {
                    	    COUMER51=(Token)match(input,COUMER,FOLLOW_COUMER_in_fun457);  
                    	    stream_COUMER.add(COUMER51);


                    	    pushFollow(FOLLOW_parma_in_fun459);
                    	    parma52=parma();

                    	    state._fsp--;

                    	    stream_parma.add(parma52.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }


            RPARENT53=(Token)match(input,RPARENT,FOLLOW_RPARENT_in_fun466);  
            stream_RPARENT.add(RPARENT53);


            // AST REWRITE
            // elements: parma, parma, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 43:48: -> ^( TOK_FUN ID ( parma ( parma )* )? )
            {
                // D:\\antlr_data\\CreateTest.g:43:51: ^( TOK_FUN ID ( parma ( parma )* )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FUN, "TOK_FUN")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                // D:\\antlr_data\\CreateTest.g:43:64: ( parma ( parma )* )?
                if ( stream_parma.hasNext()||stream_parma.hasNext() ) {
                    adaptor.addChild(root_1, stream_parma.nextTree());

                    // D:\\antlr_data\\CreateTest.g:43:71: ( parma )*
                    while ( stream_parma.hasNext() ) {
                        adaptor.addChild(root_1, stream_parma.nextTree());

                    }
                    stream_parma.reset();

                }
                stream_parma.reset();
                stream_parma.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "fun"


    public static class conditonName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "conditonName"
    // D:\\antlr_data\\CreateTest.g:45:1: conditonName : ( ID | fun );
    public final CreateTestParser.conditonName_return conditonName() throws RecognitionException {
        CreateTestParser.conditonName_return retval = new CreateTestParser.conditonName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID54=null;
        CreateTestParser.fun_return fun55 =null;


        CommonTree ID54_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:45:13: ( ID | fun )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==ID) ) {
                int LA13_1 = input.LA(2);

                if ( (LA13_1==LPARENT) ) {
                    alt13=2;
                }
                else if ( (LA13_1==EQUAL||LA13_1==IN||(LA13_1 >= IS && LA13_1 <= LIKE)||(LA13_1 >= MORE && LA13_1 <= NOTEQUAL)) ) {
                    alt13=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }
            switch (alt13) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:46:2: ID
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    ID54=(Token)match(input,ID,FOLLOW_ID_in_conditonName496); 
                    ID54_tree = 
                    (CommonTree)adaptor.create(ID54)
                    ;
                    adaptor.addChild(root_0, ID54_tree);


                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:46:5: fun
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_fun_in_conditonName498);
                    fun55=fun();

                    state._fsp--;

                    adaptor.addChild(root_0, fun55.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "conditonName"


    public static class parma_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "parma"
    // D:\\antlr_data\\CreateTest.g:48:1: parma : ( ID | INT );
    public final CreateTestParser.parma_return parma() throws RecognitionException {
        CreateTestParser.parma_return retval = new CreateTestParser.parma_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set56=null;

        CommonTree set56_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:48:7: ( ID | INT )
            // D:\\antlr_data\\CreateTest.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set56=(Token)input.LT(1);

            if ( input.LA(1)==ID||input.LA(1)==INT ) {
                input.consume();
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set56)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "parma"


    public static class allItem_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "allItem"
    // D:\\antlr_data\\CreateTest.g:51:1: allItem : ( INT | StringLiteral );
    public final CreateTestParser.allItem_return allItem() throws RecognitionException {
        CreateTestParser.allItem_return retval = new CreateTestParser.allItem_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set57=null;

        CommonTree set57_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:51:9: ( INT | StringLiteral )
            // D:\\antlr_data\\CreateTest.g:
            {
            root_0 = (CommonTree)adaptor.nil();


            set57=(Token)input.LT(1);

            if ( input.LA(1)==INT||input.LA(1)==StringLiteral ) {
                input.consume();
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(set57)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "allItem"


    public static class statement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "statement"
    // D:\\antlr_data\\CreateTest.g:54:1: statement : ( loadDataStateMent | createTableStateMent | createIndexStateMent | selectStateMent );
    public final CreateTestParser.statement_return statement() throws RecognitionException {
        CreateTestParser.statement_return retval = new CreateTestParser.statement_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        CreateTestParser.loadDataStateMent_return loadDataStateMent58 =null;

        CreateTestParser.createTableStateMent_return createTableStateMent59 =null;

        CreateTestParser.createIndexStateMent_return createIndexStateMent60 =null;

        CreateTestParser.selectStateMent_return selectStateMent61 =null;



        try {
            // D:\\antlr_data\\CreateTest.g:55:2: ( loadDataStateMent | createTableStateMent | createIndexStateMent | selectStateMent )
            int alt14=4;
            switch ( input.LA(1) ) {
            case 58:
                {
                alt14=1;
                }
                break;
            case 52:
                {
                int LA14_2 = input.LA(2);

                if ( (LA14_2==63) ) {
                    alt14=2;
                }
                else if ( (LA14_2==64) ) {
                    alt14=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 14, 2, input);

                    throw nvae;

                }
                }
                break;
            case 62:
                {
                alt14=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:55:4: loadDataStateMent
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_loadDataStateMent_in_statement539);
                    loadDataStateMent58=loadDataStateMent();

                    state._fsp--;

                    adaptor.addChild(root_0, loadDataStateMent58.getTree());

                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:55:25: createTableStateMent
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_createTableStateMent_in_statement544);
                    createTableStateMent59=createTableStateMent();

                    state._fsp--;

                    adaptor.addChild(root_0, createTableStateMent59.getTree());

                    }
                    break;
                case 3 :
                    // D:\\antlr_data\\CreateTest.g:55:48: createIndexStateMent
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_createIndexStateMent_in_statement548);
                    createIndexStateMent60=createIndexStateMent();

                    state._fsp--;

                    adaptor.addChild(root_0, createIndexStateMent60.getTree());

                    }
                    break;
                case 4 :
                    // D:\\antlr_data\\CreateTest.g:55:71: selectStateMent
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_selectStateMent_in_statement552);
                    selectStateMent61=selectStateMent();

                    state._fsp--;

                    adaptor.addChild(root_0, selectStateMent61.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "statement"


    public static class selectStateMent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "selectStateMent"
    // D:\\antlr_data\\CreateTest.g:59:1: selectStateMent : 'select' selectList 'from' tableName 'on' appName ( 'where' condition )? -> ^( TOK_SELECT tableName appName selectList ( condition )? ) ;
    public final CreateTestParser.selectStateMent_return selectStateMent() throws RecognitionException {
        CreateTestParser.selectStateMent_return retval = new CreateTestParser.selectStateMent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal62=null;
        Token string_literal64=null;
        Token string_literal66=null;
        Token string_literal68=null;
        CreateTestParser.selectList_return selectList63 =null;

        CreateTestParser.tableName_return tableName65 =null;

        CreateTestParser.appName_return appName67 =null;

        CreateTestParser.condition_return condition69 =null;


        CommonTree string_literal62_tree=null;
        CommonTree string_literal64_tree=null;
        CommonTree string_literal66_tree=null;
        CommonTree string_literal68_tree=null;
        RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_appName=new RewriteRuleSubtreeStream(adaptor,"rule appName");
        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        RewriteRuleSubtreeStream stream_tableName=new RewriteRuleSubtreeStream(adaptor,"rule tableName");
        RewriteRuleSubtreeStream stream_selectList=new RewriteRuleSubtreeStream(adaptor,"rule selectList");
        try {
            // D:\\antlr_data\\CreateTest.g:60:2: ( 'select' selectList 'from' tableName 'on' appName ( 'where' condition )? -> ^( TOK_SELECT tableName appName selectList ( condition )? ) )
            // D:\\antlr_data\\CreateTest.g:60:4: 'select' selectList 'from' tableName 'on' appName ( 'where' condition )?
            {
            string_literal62=(Token)match(input,62,FOLLOW_62_in_selectStateMent565);  
            stream_62.add(string_literal62);


            pushFollow(FOLLOW_selectList_in_selectStateMent568);
            selectList63=selectList();

            state._fsp--;

            stream_selectList.add(selectList63.getTree());

            string_literal64=(Token)match(input,54,FOLLOW_54_in_selectStateMent570);  
            stream_54.add(string_literal64);


            pushFollow(FOLLOW_tableName_in_selectStateMent572);
            tableName65=tableName();

            state._fsp--;

            stream_tableName.add(tableName65.getTree());

            string_literal66=(Token)match(input,61,FOLLOW_61_in_selectStateMent574);  
            stream_61.add(string_literal66);


            pushFollow(FOLLOW_appName_in_selectStateMent576);
            appName67=appName();

            state._fsp--;

            stream_appName.add(appName67.getTree());

            // D:\\antlr_data\\CreateTest.g:60:56: ( 'where' condition )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==65) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:60:57: 'where' condition
                    {
                    string_literal68=(Token)match(input,65,FOLLOW_65_in_selectStateMent580);  
                    stream_65.add(string_literal68);


                    pushFollow(FOLLOW_condition_in_selectStateMent582);
                    condition69=condition();

                    state._fsp--;

                    stream_condition.add(condition69.getTree());

                    }
                    break;

            }


            // AST REWRITE
            // elements: appName, condition, selectList, tableName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 60:78: -> ^( TOK_SELECT tableName appName selectList ( condition )? )
            {
                // D:\\antlr_data\\CreateTest.g:60:81: ^( TOK_SELECT tableName appName selectList ( condition )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_SELECT, "TOK_SELECT")
                , root_1);

                adaptor.addChild(root_1, stream_tableName.nextTree());

                adaptor.addChild(root_1, stream_appName.nextTree());

                adaptor.addChild(root_1, stream_selectList.nextTree());

                // D:\\antlr_data\\CreateTest.g:60:124: ( condition )?
                if ( stream_condition.hasNext() ) {
                    adaptor.addChild(root_1, stream_condition.nextTree());

                }
                stream_condition.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "selectStateMent"


    public static class selectList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "selectList"
    // D:\\antlr_data\\CreateTest.g:62:1: selectList : ID ( COUMER ID )* -> ^( TOK_SELECTLIST ( ID )* ) ;
    public final CreateTestParser.selectList_return selectList() throws RecognitionException {
        CreateTestParser.selectList_return retval = new CreateTestParser.selectList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID70=null;
        Token COUMER71=null;
        Token ID72=null;

        CommonTree ID70_tree=null;
        CommonTree COUMER71_tree=null;
        CommonTree ID72_tree=null;
        RewriteRuleTokenStream stream_COUMER=new RewriteRuleTokenStream(adaptor,"token COUMER");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // D:\\antlr_data\\CreateTest.g:63:2: ( ID ( COUMER ID )* -> ^( TOK_SELECTLIST ( ID )* ) )
            // D:\\antlr_data\\CreateTest.g:63:5: ID ( COUMER ID )*
            {
            ID70=(Token)match(input,ID,FOLLOW_ID_in_selectList614);  
            stream_ID.add(ID70);


            // D:\\antlr_data\\CreateTest.g:63:8: ( COUMER ID )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==COUMER) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:63:9: COUMER ID
            	    {
            	    COUMER71=(Token)match(input,COUMER,FOLLOW_COUMER_in_selectList617);  
            	    stream_COUMER.add(COUMER71);


            	    ID72=(Token)match(input,ID,FOLLOW_ID_in_selectList619);  
            	    stream_ID.add(ID72);


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 63:21: -> ^( TOK_SELECTLIST ( ID )* )
            {
                // D:\\antlr_data\\CreateTest.g:63:24: ^( TOK_SELECTLIST ( ID )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_SELECTLIST, "TOK_SELECTLIST")
                , root_1);

                // D:\\antlr_data\\CreateTest.g:63:41: ( ID )*
                while ( stream_ID.hasNext() ) {
                    adaptor.addChild(root_1, 
                    stream_ID.nextNode()
                    );

                }
                stream_ID.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "selectList"


    public static class loadDataStateMent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "loadDataStateMent"
    // D:\\antlr_data\\CreateTest.g:66:1: loadDataStateMent : 'load' 'data' host 'inpath' path 'into' 'table' tableName 'on' appName -> ^( TOK_LOAD appName tableName path ) ;
    public final CreateTestParser.loadDataStateMent_return loadDataStateMent() throws RecognitionException {
        CreateTestParser.loadDataStateMent_return retval = new CreateTestParser.loadDataStateMent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal73=null;
        Token string_literal74=null;
        Token string_literal76=null;
        Token string_literal78=null;
        Token string_literal79=null;
        Token string_literal81=null;
        CreateTestParser.host_return host75 =null;

        CreateTestParser.path_return path77 =null;

        CreateTestParser.tableName_return tableName80 =null;

        CreateTestParser.appName_return appName82 =null;


        CommonTree string_literal73_tree=null;
        CommonTree string_literal74_tree=null;
        CommonTree string_literal76_tree=null;
        CommonTree string_literal78_tree=null;
        CommonTree string_literal79_tree=null;
        CommonTree string_literal81_tree=null;
        RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_appName=new RewriteRuleSubtreeStream(adaptor,"rule appName");
        RewriteRuleSubtreeStream stream_host=new RewriteRuleSubtreeStream(adaptor,"rule host");
        RewriteRuleSubtreeStream stream_tableName=new RewriteRuleSubtreeStream(adaptor,"rule tableName");
        RewriteRuleSubtreeStream stream_path=new RewriteRuleSubtreeStream(adaptor,"rule path");
        try {
            // D:\\antlr_data\\CreateTest.g:67:2: ( 'load' 'data' host 'inpath' path 'into' 'table' tableName 'on' appName -> ^( TOK_LOAD appName tableName path ) )
            // D:\\antlr_data\\CreateTest.g:67:4: 'load' 'data' host 'inpath' path 'into' 'table' tableName 'on' appName
            {
            string_literal73=(Token)match(input,58,FOLLOW_58_in_loadDataStateMent642);  
            stream_58.add(string_literal73);


            string_literal74=(Token)match(input,53,FOLLOW_53_in_loadDataStateMent644);  
            stream_53.add(string_literal74);


            pushFollow(FOLLOW_host_in_loadDataStateMent646);
            host75=host();

            state._fsp--;

            stream_host.add(host75.getTree());

            string_literal76=(Token)match(input,56,FOLLOW_56_in_loadDataStateMent648);  
            stream_56.add(string_literal76);


            pushFollow(FOLLOW_path_in_loadDataStateMent650);
            path77=path();

            state._fsp--;

            stream_path.add(path77.getTree());

            string_literal78=(Token)match(input,57,FOLLOW_57_in_loadDataStateMent652);  
            stream_57.add(string_literal78);


            string_literal79=(Token)match(input,63,FOLLOW_63_in_loadDataStateMent654);  
            stream_63.add(string_literal79);


            pushFollow(FOLLOW_tableName_in_loadDataStateMent656);
            tableName80=tableName();

            state._fsp--;

            stream_tableName.add(tableName80.getTree());

            string_literal81=(Token)match(input,61,FOLLOW_61_in_loadDataStateMent658);  
            stream_61.add(string_literal81);


            pushFollow(FOLLOW_appName_in_loadDataStateMent660);
            appName82=appName();

            state._fsp--;

            stream_appName.add(appName82.getTree());

            // AST REWRITE
            // elements: path, appName, tableName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 67:75: -> ^( TOK_LOAD appName tableName path )
            {
                // D:\\antlr_data\\CreateTest.g:67:78: ^( TOK_LOAD appName tableName path )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_LOAD, "TOK_LOAD")
                , root_1);

                adaptor.addChild(root_1, stream_appName.nextTree());

                adaptor.addChild(root_1, stream_tableName.nextTree());

                adaptor.addChild(root_1, stream_path.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "loadDataStateMent"


    public static class createTableStateMent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "createTableStateMent"
    // D:\\antlr_data\\CreateTest.g:71:1: createTableStateMent : 'create' 'table' tableName LPARENT columnList RPARENT 'on' appName -> ^( TOK_CREATE appName tableName columnList ) ;
    public final CreateTestParser.createTableStateMent_return createTableStateMent() throws RecognitionException {
        CreateTestParser.createTableStateMent_return retval = new CreateTestParser.createTableStateMent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal83=null;
        Token string_literal84=null;
        Token LPARENT86=null;
        Token RPARENT88=null;
        Token string_literal89=null;
        CreateTestParser.tableName_return tableName85 =null;

        CreateTestParser.columnList_return columnList87 =null;

        CreateTestParser.appName_return appName90 =null;


        CommonTree string_literal83_tree=null;
        CommonTree string_literal84_tree=null;
        CommonTree LPARENT86_tree=null;
        CommonTree RPARENT88_tree=null;
        CommonTree string_literal89_tree=null;
        RewriteRuleTokenStream stream_LPARENT=new RewriteRuleTokenStream(adaptor,"token LPARENT");
        RewriteRuleTokenStream stream_RPARENT=new RewriteRuleTokenStream(adaptor,"token RPARENT");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_appName=new RewriteRuleSubtreeStream(adaptor,"rule appName");
        RewriteRuleSubtreeStream stream_tableName=new RewriteRuleSubtreeStream(adaptor,"rule tableName");
        RewriteRuleSubtreeStream stream_columnList=new RewriteRuleSubtreeStream(adaptor,"rule columnList");
        try {
            // D:\\antlr_data\\CreateTest.g:71:22: ( 'create' 'table' tableName LPARENT columnList RPARENT 'on' appName -> ^( TOK_CREATE appName tableName columnList ) )
            // D:\\antlr_data\\CreateTest.g:71:24: 'create' 'table' tableName LPARENT columnList RPARENT 'on' appName
            {
            string_literal83=(Token)match(input,52,FOLLOW_52_in_createTableStateMent683);  
            stream_52.add(string_literal83);


            string_literal84=(Token)match(input,63,FOLLOW_63_in_createTableStateMent685);  
            stream_63.add(string_literal84);


            pushFollow(FOLLOW_tableName_in_createTableStateMent687);
            tableName85=tableName();

            state._fsp--;

            stream_tableName.add(tableName85.getTree());

            LPARENT86=(Token)match(input,LPARENT,FOLLOW_LPARENT_in_createTableStateMent689);  
            stream_LPARENT.add(LPARENT86);


            pushFollow(FOLLOW_columnList_in_createTableStateMent691);
            columnList87=columnList();

            state._fsp--;

            stream_columnList.add(columnList87.getTree());

            RPARENT88=(Token)match(input,RPARENT,FOLLOW_RPARENT_in_createTableStateMent694);  
            stream_RPARENT.add(RPARENT88);


            string_literal89=(Token)match(input,61,FOLLOW_61_in_createTableStateMent696);  
            stream_61.add(string_literal89);


            pushFollow(FOLLOW_appName_in_createTableStateMent698);
            appName90=appName();

            state._fsp--;

            stream_appName.add(appName90.getTree());

            // AST REWRITE
            // elements: columnList, tableName, appName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 71:92: -> ^( TOK_CREATE appName tableName columnList )
            {
                // D:\\antlr_data\\CreateTest.g:71:95: ^( TOK_CREATE appName tableName columnList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_CREATE, "TOK_CREATE")
                , root_1);

                adaptor.addChild(root_1, stream_appName.nextTree());

                adaptor.addChild(root_1, stream_tableName.nextTree());

                adaptor.addChild(root_1, stream_columnList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "createTableStateMent"


    public static class createIndexStateMent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "createIndexStateMent"
    // D:\\antlr_data\\CreateTest.g:74:1: createIndexStateMent : createTimeIndexStateMent ;
    public final CreateTestParser.createIndexStateMent_return createIndexStateMent() throws RecognitionException {
        CreateTestParser.createIndexStateMent_return retval = new CreateTestParser.createIndexStateMent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        CreateTestParser.createTimeIndexStateMent_return createTimeIndexStateMent91 =null;



        try {
            // D:\\antlr_data\\CreateTest.g:75:2: ( createTimeIndexStateMent )
            // D:\\antlr_data\\CreateTest.g:75:4: createTimeIndexStateMent
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_createTimeIndexStateMent_in_createIndexStateMent723);
            createTimeIndexStateMent91=createTimeIndexStateMent();

            state._fsp--;

            adaptor.addChild(root_0, createTimeIndexStateMent91.getTree());

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "createIndexStateMent"


    public static class createTimeIndexStateMent_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "createTimeIndexStateMent"
    // D:\\antlr_data\\CreateTest.g:77:1: createTimeIndexStateMent : 'create' 'time' 'index' indexName 'on' tableName 'of' appName LPARENT timeIndexCloumnList RPARENT -> ^( TOK_CREATE_TIMEINDEX appName tableName indexName timeIndexCloumnList ) ;
    public final CreateTestParser.createTimeIndexStateMent_return createTimeIndexStateMent() throws RecognitionException {
        CreateTestParser.createTimeIndexStateMent_return retval = new CreateTestParser.createTimeIndexStateMent_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal92=null;
        Token string_literal93=null;
        Token string_literal94=null;
        Token string_literal96=null;
        Token string_literal98=null;
        Token LPARENT100=null;
        Token RPARENT102=null;
        CreateTestParser.indexName_return indexName95 =null;

        CreateTestParser.tableName_return tableName97 =null;

        CreateTestParser.appName_return appName99 =null;

        CreateTestParser.timeIndexCloumnList_return timeIndexCloumnList101 =null;


        CommonTree string_literal92_tree=null;
        CommonTree string_literal93_tree=null;
        CommonTree string_literal94_tree=null;
        CommonTree string_literal96_tree=null;
        CommonTree string_literal98_tree=null;
        CommonTree LPARENT100_tree=null;
        CommonTree RPARENT102_tree=null;
        RewriteRuleTokenStream stream_LPARENT=new RewriteRuleTokenStream(adaptor,"token LPARENT");
        RewriteRuleTokenStream stream_RPARENT=new RewriteRuleTokenStream(adaptor,"token RPARENT");
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_appName=new RewriteRuleSubtreeStream(adaptor,"rule appName");
        RewriteRuleSubtreeStream stream_tableName=new RewriteRuleSubtreeStream(adaptor,"rule tableName");
        RewriteRuleSubtreeStream stream_indexName=new RewriteRuleSubtreeStream(adaptor,"rule indexName");
        RewriteRuleSubtreeStream stream_timeIndexCloumnList=new RewriteRuleSubtreeStream(adaptor,"rule timeIndexCloumnList");
        try {
            // D:\\antlr_data\\CreateTest.g:78:2: ( 'create' 'time' 'index' indexName 'on' tableName 'of' appName LPARENT timeIndexCloumnList RPARENT -> ^( TOK_CREATE_TIMEINDEX appName tableName indexName timeIndexCloumnList ) )
            // D:\\antlr_data\\CreateTest.g:78:4: 'create' 'time' 'index' indexName 'on' tableName 'of' appName LPARENT timeIndexCloumnList RPARENT
            {
            string_literal92=(Token)match(input,52,FOLLOW_52_in_createTimeIndexStateMent733);  
            stream_52.add(string_literal92);


            string_literal93=(Token)match(input,64,FOLLOW_64_in_createTimeIndexStateMent735);  
            stream_64.add(string_literal93);


            string_literal94=(Token)match(input,55,FOLLOW_55_in_createTimeIndexStateMent737);  
            stream_55.add(string_literal94);


            pushFollow(FOLLOW_indexName_in_createTimeIndexStateMent739);
            indexName95=indexName();

            state._fsp--;

            stream_indexName.add(indexName95.getTree());

            string_literal96=(Token)match(input,61,FOLLOW_61_in_createTimeIndexStateMent741);  
            stream_61.add(string_literal96);


            pushFollow(FOLLOW_tableName_in_createTimeIndexStateMent743);
            tableName97=tableName();

            state._fsp--;

            stream_tableName.add(tableName97.getTree());

            string_literal98=(Token)match(input,60,FOLLOW_60_in_createTimeIndexStateMent745);  
            stream_60.add(string_literal98);


            pushFollow(FOLLOW_appName_in_createTimeIndexStateMent747);
            appName99=appName();

            state._fsp--;

            stream_appName.add(appName99.getTree());

            LPARENT100=(Token)match(input,LPARENT,FOLLOW_LPARENT_in_createTimeIndexStateMent749);  
            stream_LPARENT.add(LPARENT100);


            pushFollow(FOLLOW_timeIndexCloumnList_in_createTimeIndexStateMent751);
            timeIndexCloumnList101=timeIndexCloumnList();

            state._fsp--;

            stream_timeIndexCloumnList.add(timeIndexCloumnList101.getTree());

            RPARENT102=(Token)match(input,RPARENT,FOLLOW_RPARENT_in_createTimeIndexStateMent754);  
            stream_RPARENT.add(RPARENT102);


            // AST REWRITE
            // elements: timeIndexCloumnList, indexName, appName, tableName
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 78:103: -> ^( TOK_CREATE_TIMEINDEX appName tableName indexName timeIndexCloumnList )
            {
                // D:\\antlr_data\\CreateTest.g:78:106: ^( TOK_CREATE_TIMEINDEX appName tableName indexName timeIndexCloumnList )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_CREATE_TIMEINDEX, "TOK_CREATE_TIMEINDEX")
                , root_1);

                adaptor.addChild(root_1, stream_appName.nextTree());

                adaptor.addChild(root_1, stream_tableName.nextTree());

                adaptor.addChild(root_1, stream_indexName.nextTree());

                adaptor.addChild(root_1, stream_timeIndexCloumnList.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "createTimeIndexStateMent"


    public static class columnList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "columnList"
    // D:\\antlr_data\\CreateTest.g:81:1: columnList : column ( COUMER ! column )* ;
    public final CreateTestParser.columnList_return columnList() throws RecognitionException {
        CreateTestParser.columnList_return retval = new CreateTestParser.columnList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token COUMER104=null;
        CreateTestParser.column_return column103 =null;

        CreateTestParser.column_return column105 =null;


        CommonTree COUMER104_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:81:11: ( column ( COUMER ! column )* )
            // D:\\antlr_data\\CreateTest.g:81:14: column ( COUMER ! column )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_column_in_columnList779);
            column103=column();

            state._fsp--;

            adaptor.addChild(root_0, column103.getTree());

            // D:\\antlr_data\\CreateTest.g:81:21: ( COUMER ! column )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==COUMER) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:81:22: COUMER ! column
            	    {
            	    COUMER104=(Token)match(input,COUMER,FOLLOW_COUMER_in_columnList782); 

            	    pushFollow(FOLLOW_column_in_columnList785);
            	    column105=column();

            	    state._fsp--;

            	    adaptor.addChild(root_0, column105.getTree());

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "columnList"


    public static class column_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "column"
    // D:\\antlr_data\\CreateTest.g:84:1: column : ID type -> ^( TOK_COLUMN ID type ) ;
    public final CreateTestParser.column_return column() throws RecognitionException {
        CreateTestParser.column_return retval = new CreateTestParser.column_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID106=null;
        CreateTestParser.type_return type107 =null;


        CommonTree ID106_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // D:\\antlr_data\\CreateTest.g:84:8: ( ID type -> ^( TOK_COLUMN ID type ) )
            // D:\\antlr_data\\CreateTest.g:84:11: ID type
            {
            ID106=(Token)match(input,ID,FOLLOW_ID_in_column800);  
            stream_ID.add(ID106);


            pushFollow(FOLLOW_type_in_column802);
            type107=type();

            state._fsp--;

            stream_type.add(type107.getTree());

            // AST REWRITE
            // elements: ID, type
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 84:19: -> ^( TOK_COLUMN ID type )
            {
                // D:\\antlr_data\\CreateTest.g:84:22: ^( TOK_COLUMN ID type )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_COLUMN, "TOK_COLUMN")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_1, stream_type.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "column"


    public static class type_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "type"
    // D:\\antlr_data\\CreateTest.g:87:1: type : ( 'LONG' | 'INT' | ( 'STRING' INT ) );
    public final CreateTestParser.type_return type() throws RecognitionException {
        CreateTestParser.type_return retval = new CreateTestParser.type_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal108=null;
        Token string_literal109=null;
        Token string_literal110=null;
        Token INT111=null;

        CommonTree string_literal108_tree=null;
        CommonTree string_literal109_tree=null;
        CommonTree string_literal110_tree=null;
        CommonTree INT111_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:87:9: ( 'LONG' | 'INT' | ( 'STRING' INT ) )
            int alt18=3;
            switch ( input.LA(1) ) {
            case 50:
                {
                alt18=1;
                }
                break;
            case 49:
                {
                alt18=2;
                }
                break;
            case 51:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // D:\\antlr_data\\CreateTest.g:87:11: 'LONG'
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    string_literal108=(Token)match(input,50,FOLLOW_50_in_type826); 
                    string_literal108_tree = 
                    (CommonTree)adaptor.create(string_literal108)
                    ;
                    adaptor.addChild(root_0, string_literal108_tree);


                    }
                    break;
                case 2 :
                    // D:\\antlr_data\\CreateTest.g:87:18: 'INT'
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    string_literal109=(Token)match(input,49,FOLLOW_49_in_type828); 
                    string_literal109_tree = 
                    (CommonTree)adaptor.create(string_literal109)
                    ;
                    adaptor.addChild(root_0, string_literal109_tree);


                    }
                    break;
                case 3 :
                    // D:\\antlr_data\\CreateTest.g:87:24: ( 'STRING' INT )
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    // D:\\antlr_data\\CreateTest.g:87:24: ( 'STRING' INT )
                    // D:\\antlr_data\\CreateTest.g:87:25: 'STRING' INT
                    {
                    string_literal110=(Token)match(input,51,FOLLOW_51_in_type831); 
                    string_literal110_tree = 
                    (CommonTree)adaptor.create(string_literal110)
                    ;
                    adaptor.addChild(root_0, string_literal110_tree);


                    INT111=(Token)match(input,INT,FOLLOW_INT_in_type833); 
                    INT111_tree = 
                    (CommonTree)adaptor.create(INT111)
                    ;
                    adaptor.addChild(root_0, INT111_tree);


                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "type"


    public static class timeIndexCloumnList_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "timeIndexCloumnList"
    // D:\\antlr_data\\CreateTest.g:89:1: timeIndexCloumnList : timeIndexcolumn ( COUMER ! timeIndexOthercolumn )* ;
    public final CreateTestParser.timeIndexCloumnList_return timeIndexCloumnList() throws RecognitionException {
        CreateTestParser.timeIndexCloumnList_return retval = new CreateTestParser.timeIndexCloumnList_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token COUMER113=null;
        CreateTestParser.timeIndexcolumn_return timeIndexcolumn112 =null;

        CreateTestParser.timeIndexOthercolumn_return timeIndexOthercolumn114 =null;


        CommonTree COUMER113_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:90:2: ( timeIndexcolumn ( COUMER ! timeIndexOthercolumn )* )
            // D:\\antlr_data\\CreateTest.g:90:4: timeIndexcolumn ( COUMER ! timeIndexOthercolumn )*
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_timeIndexcolumn_in_timeIndexCloumnList843);
            timeIndexcolumn112=timeIndexcolumn();

            state._fsp--;

            adaptor.addChild(root_0, timeIndexcolumn112.getTree());

            // D:\\antlr_data\\CreateTest.g:90:20: ( COUMER ! timeIndexOthercolumn )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==COUMER) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // D:\\antlr_data\\CreateTest.g:90:21: COUMER ! timeIndexOthercolumn
            	    {
            	    COUMER113=(Token)match(input,COUMER,FOLLOW_COUMER_in_timeIndexCloumnList846); 

            	    pushFollow(FOLLOW_timeIndexOthercolumn_in_timeIndexCloumnList849);
            	    timeIndexOthercolumn114=timeIndexOthercolumn();

            	    state._fsp--;

            	    adaptor.addChild(root_0, timeIndexOthercolumn114.getTree());

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "timeIndexCloumnList"


    public static class timeIndexcolumn_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "timeIndexcolumn"
    // D:\\antlr_data\\CreateTest.g:92:1: timeIndexcolumn : timeIndexCloumnType ID type -> ^( TOK_COLUMN ID type timeIndexCloumnType ) ;
    public final CreateTestParser.timeIndexcolumn_return timeIndexcolumn() throws RecognitionException {
        CreateTestParser.timeIndexcolumn_return retval = new CreateTestParser.timeIndexcolumn_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID116=null;
        CreateTestParser.timeIndexCloumnType_return timeIndexCloumnType115 =null;

        CreateTestParser.type_return type117 =null;


        CommonTree ID116_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_timeIndexCloumnType=new RewriteRuleSubtreeStream(adaptor,"rule timeIndexCloumnType");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // D:\\antlr_data\\CreateTest.g:92:17: ( timeIndexCloumnType ID type -> ^( TOK_COLUMN ID type timeIndexCloumnType ) )
            // D:\\antlr_data\\CreateTest.g:92:19: timeIndexCloumnType ID type
            {
            pushFollow(FOLLOW_timeIndexCloumnType_in_timeIndexcolumn861);
            timeIndexCloumnType115=timeIndexCloumnType();

            state._fsp--;

            stream_timeIndexCloumnType.add(timeIndexCloumnType115.getTree());

            ID116=(Token)match(input,ID,FOLLOW_ID_in_timeIndexcolumn864);  
            stream_ID.add(ID116);


            pushFollow(FOLLOW_type_in_timeIndexcolumn866);
            type117=type();

            state._fsp--;

            stream_type.add(type117.getTree());

            // AST REWRITE
            // elements: timeIndexCloumnType, ID, type
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 92:48: -> ^( TOK_COLUMN ID type timeIndexCloumnType )
            {
                // D:\\antlr_data\\CreateTest.g:92:51: ^( TOK_COLUMN ID type timeIndexCloumnType )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_COLUMN, "TOK_COLUMN")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_1, stream_type.nextTree());

                adaptor.addChild(root_1, stream_timeIndexCloumnType.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "timeIndexcolumn"


    public static class timeIndexOthercolumn_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "timeIndexOthercolumn"
    // D:\\antlr_data\\CreateTest.g:94:1: timeIndexOthercolumn : ID type -> ^( TOK_COLUMN ID type ) ;
    public final CreateTestParser.timeIndexOthercolumn_return timeIndexOthercolumn() throws RecognitionException {
        CreateTestParser.timeIndexOthercolumn_return retval = new CreateTestParser.timeIndexOthercolumn_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID118=null;
        CreateTestParser.type_return type119 =null;


        CommonTree ID118_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");
        try {
            // D:\\antlr_data\\CreateTest.g:94:22: ( ID type -> ^( TOK_COLUMN ID type ) )
            // D:\\antlr_data\\CreateTest.g:94:25: ID type
            {
            ID118=(Token)match(input,ID,FOLLOW_ID_in_timeIndexOthercolumn889);  
            stream_ID.add(ID118);


            pushFollow(FOLLOW_type_in_timeIndexOthercolumn891);
            type119=type();

            state._fsp--;

            stream_type.add(type119.getTree());

            // AST REWRITE
            // elements: type, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 94:33: -> ^( TOK_COLUMN ID type )
            {
                // D:\\antlr_data\\CreateTest.g:94:36: ^( TOK_COLUMN ID type )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_COLUMN, "TOK_COLUMN")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_1, stream_type.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "timeIndexOthercolumn"


    public static class timeIndexCloumnType_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "timeIndexCloumnType"
    // D:\\antlr_data\\CreateTest.g:96:1: timeIndexCloumnType : 'time' ;
    public final CreateTestParser.timeIndexCloumnType_return timeIndexCloumnType() throws RecognitionException {
        CreateTestParser.timeIndexCloumnType_return retval = new CreateTestParser.timeIndexCloumnType_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal120=null;

        CommonTree string_literal120_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:97:2: ( 'time' )
            // D:\\antlr_data\\CreateTest.g:97:4: 'time'
            {
            root_0 = (CommonTree)adaptor.nil();


            string_literal120=(Token)match(input,64,FOLLOW_64_in_timeIndexCloumnType913); 
            string_literal120_tree = 
            (CommonTree)adaptor.create(string_literal120)
            ;
            adaptor.addChild(root_0, string_literal120_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "timeIndexCloumnType"


    public static class appName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "appName"
    // D:\\antlr_data\\CreateTest.g:100:1: appName : ID -> ^( TOK_APP ID ) ;
    public final CreateTestParser.appName_return appName() throws RecognitionException {
        CreateTestParser.appName_return retval = new CreateTestParser.appName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID121=null;

        CommonTree ID121_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // D:\\antlr_data\\CreateTest.g:100:9: ( ID -> ^( TOK_APP ID ) )
            // D:\\antlr_data\\CreateTest.g:100:11: ID
            {
            ID121=(Token)match(input,ID,FOLLOW_ID_in_appName923);  
            stream_ID.add(ID121);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 100:13: -> ^( TOK_APP ID )
            {
                // D:\\antlr_data\\CreateTest.g:100:15: ^( TOK_APP ID )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_APP, "TOK_APP")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "appName"


    public static class tableName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "tableName"
    // D:\\antlr_data\\CreateTest.g:102:1: tableName : ID -> ^( TOK_FROM ID ) ;
    public final CreateTestParser.tableName_return tableName() throws RecognitionException {
        CreateTestParser.tableName_return retval = new CreateTestParser.tableName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID122=null;

        CommonTree ID122_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // D:\\antlr_data\\CreateTest.g:102:10: ( ID -> ^( TOK_FROM ID ) )
            // D:\\antlr_data\\CreateTest.g:102:12: ID
            {
            ID122=(Token)match(input,ID,FOLLOW_ID_in_tableName936);  
            stream_ID.add(ID122);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 102:15: -> ^( TOK_FROM ID )
            {
                // D:\\antlr_data\\CreateTest.g:102:17: ^( TOK_FROM ID )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_FROM, "TOK_FROM")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tableName"


    public static class indexName_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "indexName"
    // D:\\antlr_data\\CreateTest.g:104:1: indexName : ID -> ^( TOK_INDEX ID ) ;
    public final CreateTestParser.indexName_return indexName() throws RecognitionException {
        CreateTestParser.indexName_return retval = new CreateTestParser.indexName_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token ID123=null;

        CommonTree ID123_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // D:\\antlr_data\\CreateTest.g:105:2: ( ID -> ^( TOK_INDEX ID ) )
            // D:\\antlr_data\\CreateTest.g:105:4: ID
            {
            ID123=(Token)match(input,ID,FOLLOW_ID_in_indexName952);  
            stream_ID.add(ID123);


            // AST REWRITE
            // elements: ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 105:7: -> ^( TOK_INDEX ID )
            {
                // D:\\antlr_data\\CreateTest.g:105:9: ^( TOK_INDEX ID )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TOK_INDEX, "TOK_INDEX")
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "indexName"


    public static class host_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "host"
    // D:\\antlr_data\\CreateTest.g:107:1: host : 'local' ;
    public final CreateTestParser.host_return host() throws RecognitionException {
        CreateTestParser.host_return retval = new CreateTestParser.host_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token string_literal124=null;

        CommonTree string_literal124_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:107:6: ( 'local' )
            // D:\\antlr_data\\CreateTest.g:107:8: 'local'
            {
            root_0 = (CommonTree)adaptor.nil();


            string_literal124=(Token)match(input,59,FOLLOW_59_in_host967); 
            string_literal124_tree = 
            (CommonTree)adaptor.create(string_literal124)
            ;
            adaptor.addChild(root_0, string_literal124_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "host"


    public static class path_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "path"
    // D:\\antlr_data\\CreateTest.g:109:1: path : StringLiteral ;
    public final CreateTestParser.path_return path() throws RecognitionException {
        CreateTestParser.path_return retval = new CreateTestParser.path_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token StringLiteral125=null;

        CommonTree StringLiteral125_tree=null;

        try {
            // D:\\antlr_data\\CreateTest.g:109:9: ( StringLiteral )
            // D:\\antlr_data\\CreateTest.g:109:11: StringLiteral
            {
            root_0 = (CommonTree)adaptor.nil();


            StringLiteral125=(Token)match(input,StringLiteral,FOLLOW_StringLiteral_in_path980); 
            StringLiteral125_tree = 
            (CommonTree)adaptor.create(StringLiteral125)
            ;
            adaptor.addChild(root_0, StringLiteral125_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "path"

    // Delegated rules


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\22\uffff";
    static final String DFA10_eofS =
        "\22\uffff";
    static final String DFA10_minS =
        "\1\14\1\7\1\14\4\uffff\1\15\1\22\2\uffff\1\6\1\7\1\16\1\14\2\uffff"+
        "\1\6";
    static final String DFA10_maxS =
        "\1\14\1\25\1\33\4\uffff\1\15\1\22\2\uffff\1\33\1\25\1\35\1\16\2"+
        "\uffff\1\33";
    static final String DFA10_acceptS =
        "\3\uffff\1\1\1\2\1\3\1\4\2\uffff\1\7\1\10\4\uffff\1\5\1\6\1\uffff";
    static final String DFA10_specialS =
        "\22\uffff}>";
    static final String[] DFA10_transitionS = {
            "\1\1",
            "\1\4\5\uffff\1\10\1\uffff\1\11\1\6\1\12\1\2\1\3\1\7\1\5",
            "\1\13\1\uffff\1\13\14\uffff\1\14",
            "",
            "",
            "",
            "",
            "\1\10",
            "\1\15",
            "",
            "",
            "\1\16\24\uffff\1\14",
            "\1\4\5\uffff\1\10\1\uffff\1\11\1\6\1\12\1\uffff\1\3\1\7\1\5",
            "\1\17\16\uffff\1\20",
            "\1\21\1\uffff\1\21",
            "",
            "",
            "\1\16\24\uffff\1\14"
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "24:1: conditionItem : ( conditonName MORE INT -> ^( TOK_ATOMCONDITION conditonName MORE INT ) | conditonName EQUAL allItem -> ^( TOK_ATOMCONDITION conditonName EQUAL allItem ) | conditonName NOTEQUAL allItem -> ^( TOK_ATOMCONDITION conditonName NOTEQUAL allItem ) | conditonName LESS INT -> ^( TOK_ATOMCONDITION conditonName LESS INT ) | conditonName ( NOT )? IN LPARENT INT ( COUMER INT )* RPARENT -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( INT )* ) | conditonName ( NOT )? IN LPARENT StringLiteral ( COUMER StringLiteral )* RPARENT -> ^( TOK_ATOMCONDITION conditonName IN ( NOT )? ( StringLiteral )* ) | conditonName IS ( NOT )? NULL -> ^( TOK_ATOMCONDITION conditonName IS ( NOT )? ) | conditonName LIKE StringLiteral -> ^( TOK_ATOMCONDITION conditonName LIKE StringLiteral ) );";
        }
    }
 

    public static final BitSet FOLLOW_orcondition_in_condition82 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_andcondtion_in_orcondition95 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_OROPERATER_in_orcondition98 = new BitSet(new long[]{0x0000000000041000L});
    public static final BitSet FOLLOW_andcondtion_in_orcondition100 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_conditionItem_in_andcondtion128 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ANDOPERATER_in_andcondtion131 = new BitSet(new long[]{0x0000000000041000L});
    public static final BitSet FOLLOW_andcondtion_in_andcondtion134 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_LPARENT_in_andcondtion157 = new BitSet(new long[]{0x0000000000041000L});
    public static final BitSet FOLLOW_orcondition_in_andcondtion159 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPARENT_in_andcondtion163 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_ANDOPERATER_in_andcondtion167 = new BitSet(new long[]{0x0000000000041000L});
    public static final BitSet FOLLOW_andcondtion_in_andcondtion170 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem202 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_MORE_in_conditionItem204 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_INT_in_conditionItem206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem228 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_EQUAL_in_conditionItem230 = new BitSet(new long[]{0x0000000020004000L});
    public static final BitSet FOLLOW_allItem_in_conditionItem232 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem255 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_NOTEQUAL_in_conditionItem257 = new BitSet(new long[]{0x0000000020004000L});
    public static final BitSet FOLLOW_allItem_in_conditionItem259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem280 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_LESS_in_conditionItem282 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_INT_in_conditionItem284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem307 = new BitSet(new long[]{0x0000000000102000L});
    public static final BitSet FOLLOW_NOT_in_conditionItem309 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_IN_in_conditionItem312 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LPARENT_in_conditionItem315 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_INT_in_conditionItem317 = new BitSet(new long[]{0x0000000008000040L});
    public static final BitSet FOLLOW_COUMER_in_conditionItem319 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_INT_in_conditionItem321 = new BitSet(new long[]{0x0000000008000040L});
    public static final BitSet FOLLOW_RPARENT_in_conditionItem325 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem350 = new BitSet(new long[]{0x0000000000102000L});
    public static final BitSet FOLLOW_NOT_in_conditionItem353 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_IN_in_conditionItem356 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LPARENT_in_conditionItem359 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_StringLiteral_in_conditionItem361 = new BitSet(new long[]{0x0000000008000040L});
    public static final BitSet FOLLOW_COUMER_in_conditionItem363 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_StringLiteral_in_conditionItem365 = new BitSet(new long[]{0x0000000008000040L});
    public static final BitSet FOLLOW_RPARENT_in_conditionItem369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem392 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_IS_in_conditionItem394 = new BitSet(new long[]{0x0000000000500000L});
    public static final BitSet FOLLOW_NOT_in_conditionItem396 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_NULL_in_conditionItem399 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditonName_in_conditionItem419 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_LIKE_in_conditionItem421 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_StringLiteral_in_conditionItem423 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_fun448 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LPARENT_in_fun450 = new BitSet(new long[]{0x0000000008005000L});
    public static final BitSet FOLLOW_parma_in_fun454 = new BitSet(new long[]{0x0000000008000040L});
    public static final BitSet FOLLOW_COUMER_in_fun457 = new BitSet(new long[]{0x0000000000005000L});
    public static final BitSet FOLLOW_parma_in_fun459 = new BitSet(new long[]{0x0000000008000040L});
    public static final BitSet FOLLOW_RPARENT_in_fun466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_conditonName496 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fun_in_conditonName498 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_loadDataStateMent_in_statement539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createTableStateMent_in_statement544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createIndexStateMent_in_statement548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_selectStateMent_in_statement552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_selectStateMent565 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_selectList_in_selectStateMent568 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_selectStateMent570 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_tableName_in_selectStateMent572 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_61_in_selectStateMent574 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_appName_in_selectStateMent576 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_selectStateMent580 = new BitSet(new long[]{0x0000000000041000L});
    public static final BitSet FOLLOW_condition_in_selectStateMent582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_selectList614 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_COUMER_in_selectList617 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ID_in_selectList619 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_58_in_loadDataStateMent642 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_loadDataStateMent644 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_host_in_loadDataStateMent646 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_loadDataStateMent648 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_path_in_loadDataStateMent650 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_loadDataStateMent652 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_loadDataStateMent654 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_tableName_in_loadDataStateMent656 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_61_in_loadDataStateMent658 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_appName_in_loadDataStateMent660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_createTableStateMent683 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_63_in_createTableStateMent685 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_tableName_in_createTableStateMent687 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LPARENT_in_createTableStateMent689 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_columnList_in_createTableStateMent691 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPARENT_in_createTableStateMent694 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_61_in_createTableStateMent696 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_appName_in_createTableStateMent698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createTimeIndexStateMent_in_createIndexStateMent723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_createTimeIndexStateMent733 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_createTimeIndexStateMent735 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_createTimeIndexStateMent737 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_indexName_in_createTimeIndexStateMent739 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_61_in_createTimeIndexStateMent741 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_tableName_in_createTimeIndexStateMent743 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_60_in_createTimeIndexStateMent745 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_appName_in_createTimeIndexStateMent747 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_LPARENT_in_createTimeIndexStateMent749 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_timeIndexCloumnList_in_createTimeIndexStateMent751 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_RPARENT_in_createTimeIndexStateMent754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_in_columnList779 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_COUMER_in_columnList782 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_column_in_columnList785 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_ID_in_column800 = new BitSet(new long[]{0x000E000000000000L});
    public static final BitSet FOLLOW_type_in_column802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_type826 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_type828 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_type831 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_INT_in_type833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_timeIndexcolumn_in_timeIndexCloumnList843 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_COUMER_in_timeIndexCloumnList846 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_timeIndexOthercolumn_in_timeIndexCloumnList849 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_timeIndexCloumnType_in_timeIndexcolumn861 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ID_in_timeIndexcolumn864 = new BitSet(new long[]{0x000E000000000000L});
    public static final BitSet FOLLOW_type_in_timeIndexcolumn866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_timeIndexOthercolumn889 = new BitSet(new long[]{0x000E000000000000L});
    public static final BitSet FOLLOW_type_in_timeIndexOthercolumn891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_timeIndexCloumnType913 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_appName923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_tableName936 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_indexName952 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_host967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_path980 = new BitSet(new long[]{0x0000000000000002L});

}