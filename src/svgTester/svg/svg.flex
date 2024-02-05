package svgTester.svg;
import java_cup.runtime.*;

%%

%cup
%line
%column
%unicode
%class Lexer

%{

/**
 * Return a new Symbol with the given token id, and with the current line and
 * column numbers.
 */
Symbol newSym(int tokenId) {
    return new Symbol(tokenId, yyline, yycolumn);
}

/**
 * Return a new Symbol with the given token id, the current line and column
 * numbers, and the given token value.  The value is used for tokens such as
 * identifiers and numbers.
 */
Symbol newSym(int tokenId, Object value) {
    return new Symbol(tokenId, yyline, yycolumn, value);
}

%}

/* basic Defs */
digit           = [0-9]
integer			= [-]?(0|[1-9][0-9]*)
real			= {integer}"."[0-9]+
letter          = [A-Za-z]
alphanumeric    = {letter}|{digit}
sonder			= [äöüÄÖÜß]
other_id_char   = [\_\-\:\+\/\.\(\)]
/*char            = '.'*/
/*leftbrace       = \{*/
/*rightbrace      = \}*/

/* deep r  */
identifier      = ({letter}|{alphanumeric}|{other_id_char}|{sonder})*
newLines		= \n|\r|\r\n
inputChar		= [^\r\n]
whiteSpace		= {newLines}|[ \t\f]

url				= (http[s]?:\/\/)(\w+)(\.|\/|(\w|\d|\#|\-))+
color			= "#"[0-9a-fA-F]{6}
openTag			= <[A-Za-z](([A-Za-z]|[0-9])|[\_\-\:]|[äöüÄÖÜß])*[>]?
closeTag		= ">"
endTag			= "/>"|("</"{identifier}">")
headerTag		= "<?"(.+)"?>"

/* comments */
comment 		= {lineComment} | {moreLineComment} | {docComment}
lineComment 	= "//" {inputChar}* {newLines}?
moreLineComment	= "/*" [^*] ~"*/" | "/*" "*"+ "/"
docComment		= "/**" {commentContent} "*"+ "/"
htmlComment		= "<!--"(.*)"-->"
commentContent	= ( [^*] | \*+ [^/*] )*

%state STRING, VALUE

%%
<YYINITIAL> {

{htmlComment}			{ /* ignore */ }
{comment}				{ /* ignore */ }
{whiteSpace}			{ /* ignore */ }

/*\"					{ return newSym(sym.ANF, yytext()); } */
\"						{ yybegin(STRING); return newSym(sym.ANF, "ANF_AUF"); }
"="						{ return newSym(sym.EQ, yytext()); }
/*":"					{ return newSym(sym.COLON, yytext()); }*/
/*";"					{ /* ignore */ }*/

{color}					{ return newSym(sym.COLOR, yytext()); }
{headerTag}				{ return newSym(sym.HEADERTAG, yytext()); }
{openTag}				{ return newSym(sym.OPENTAG, yytext()); }
{closeTag}				{ return newSym(sym.CLOSETAG, yytext()); }
{endTag}				{ return newSym(sym.ENDTAG, yytext()); }

{integer}       		{ return newSym(sym.INT, new Integer(yytext())); }
{real}          		{ return newSym(sym.REAL, new Double(yytext())); }
{identifier}			{ return newSym(sym.IDENT, yytext()); }

}

<STRING> {
{url}					{ return newSym(sym.URL, yytext()); }
[^\":]+					{ return newSym(sym.CHAR, yytext());}
":"						{ yybegin(VALUE); return newSym(sym.COLON, yytext()); }
\"						{ yybegin(YYINITIAL); return newSym(sym.ANF, "ANF_ZU"); }
/*
";"						{ return newSym(sym.SEMICOLON, yytext()); }
","						{ return newSym(sym.COMMA, yytext()); }

{color}					{ return newSym(sym.COLOR, yytext()); }
{closeTag}				{ return newSym(sym.CLOSETAG, yytext()); }
{integer}       		{ return newSym(sym.INT, new Integer(yytext())); }
{real}          		{ return newSym(sym.REAL, new Double(yytext())); }
{identifier}			{ return newSym(sym.IDENT, yytext()); }*/

}

<VALUE> {
[^\";]+					{ return newSym(sym.VALUE, yytext());}
\"						{ yybegin(YYINITIAL); return newSym(sym.ANF, "ANF_ZU"); }
";"						{ yybegin(STRING); return newSym(sym.SEMICOLON, yytext()); }

}

[^]						{ return newSym(sym.ZERO, "ERROR: "+yytext());  }

/* error fallback */
/*[^]					{ throw new Error("Illegal character <\""+yytext()+"\">");}*/