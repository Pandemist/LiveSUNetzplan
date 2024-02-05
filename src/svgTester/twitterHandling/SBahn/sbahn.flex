package svgTester.twitterHandling.SBahn;
import java_cup.runtime.*;

%%

%cup
%line
%column
%unicode
%class SLexer

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

/* Basic Defs */
line        	= S([1359]|[78][5]?|4[12567]{1}|2[56]?)
digit           = [0-9]
letter          = [A-Za-z]
alphanumeric    = {letter}|{digit}
sonder			= [äöüÄÖÜß]
other_id_char   = [\_\-\:\+\/\.\(\)]
identifier      = ({letter}|{alphanumeric}|{other_id_char}|{sonder})+
url				= (http[s]?:\/\/)[.]+
newLines		= \n|\r|\r\n
whiteSpace		= {newLines}|[ \t\f]

/* Level 2 Defs */

sline      	 	= "#"{line}
at 				= "@"{identifier}
hashtag			= "#"{identifier}

%%

{sline}                 { return newSym(sym.LINE, yytext());	}
{at}					{ return newSym(sym.AT, yytext());	}
{hashtag}				{ return newSym(sym.HASH, yytext());	}
{url}					{ return newSym(sym.URL, yytext());	}
{identifier}			{ return newSym(sym.WORD, yytext());	}


{whiteSpace}			{ /* ignore */	}


[^]						{ return newSym(sym.ZERO, "ERROR: "+yytext());  }