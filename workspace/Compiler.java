/* =====================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Compiladores - 2016/1
 * Orientação: Prof.ª Dr.ª Tiemi C. Sakata
 * 
 * Analise sintatica para a linguagem da seção 3
 *
 * Trabalho - Fase final
 *
 * Março de 2016
 * 
 * Gabriel Stankevix Soares		|	511340
 * Luis Augusto França Barbosa	|	511374
===================================================================== */

import AuxComp.*; 
import AST.*;
import Lexer.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.*;
import java.io.*;


public class Compiler {

    // Contem as keywords/palavras chaves do sistema
   // static private Hashtable <String, Symbol> keywordsTable;
    
    public Program compile( char []input, PrintWriter outError, String arquivo_erro) {
        //instancia a nova HashTable para variaveis 
        symbolTable = new SymbolTable();
        error = new CompilerError(lexer, new PrintWriter(outError), arquivo_erro);
        lexer = new Lexer(input, error);
        error.setLexer(lexer);
        
        
        lexer.nextToken();  
        return program();
    }

	//Program ::= Decl
    private Program program(){
        //Este ArrayList contem todas as funcoes do pgm
        //ArrayList<Subroutine> funcList = new ArrayList<Subroutine>();
        
        ArrayList<Decl>declList = new ArrayList<Decl>();
        Decl decl_aux = null;
        
        Decl decl = Decl();
        symbolTable.localTable.clear();       
        while(true){
            decl_aux = Decl();
            if(decl_aux == null){
                break;
            }
            declList.add(decl_aux);
            symbolTable.localTable.clear();  
        }
        
        //VERIFICAR EXISTEM FUNÇÕES MAIN DECLARADAS NO PGM
        Object value = symbolTable.getInGlobal("main");
        if(value == null){
            error.signal("Error: Program must have a MAIN function!");
        }
        return new Program(decl, declList);
    }
    
    
    //Dec ::=  FunctionDecl
    private Decl Decl(){
        FunctionDecl function = FunctionDecl();
        if(function == null){
            return null;
        }
        
        return new Decl(function);
    }
 
    
     // StmtBlock ::= ‘{’ { VariableDecl } { Stmt } ‘}’
    private StmtBlock StmtBlock(){
        Stmt stmt = null;
        ArrayList<VariableDecl>  variableDeclList = new ArrayList<VariableDecl>();
        ArrayList<Stmt> stmtList = new ArrayList<Stmt>();

        if(lexer.token == Symbol.LEFTBRACES){
            lexer.nextToken();
            if(lexer.token== Symbol.SEMICOLON)
                error.signal("Error: ';' isn't expected!");
            
            //{VariableDecl}
            while(true){
                if(lexer.token != Symbol.INT && lexer.token != Symbol.DOUBLE && lexer.token != Symbol.CHAR){
                    break;
                }
                variableDeclList.add(VariableDecl());
            }

            //{Stmt}
            while(true){
                stmt = Stmt();
                if(stmt == null){
                    break;
                }
                stmtList.add(stmt);
            }
            

            if(lexer.token == Symbol.RIGHTBRACES){
                
                if(currentFunction!= null && variableDeclList.isEmpty() && stmtList.isEmpty()){
                    error.signal("Error: This function must have a return!");
                }
                
                lexer.nextToken();
                
                return new StmtBlock(variableDeclList, stmtList);

            }
        }
        error.signal("Error: '}' expected!");
        return null;
    }


    //VariableDecl::=  Variable ';'
    private VariableDecl VariableDecl() {
        Variable variable = null;
        
        variable = Variable();
        
        //Trata o caso a; onde a não tem tipo
        Type type = variable.getVariableType();
        if(type == null){
            error.signal("Error: You must declare the type!");
        }
        //Verifica se a variavel ja foi declarada na tabela de variaveis 
        nameVariable = lexer.getNameVariable();
        Object value = symbolTable.getInLocal(nameVariable);//ARRUMARSAPORRA  
      
        Symbol keyword = lexer.keywordsTable.get(nameVariable);
        
        if(keyword != null){
            error.signal("Error: Keyword can't be used!");
        }

        if(value == null){
            symbolTable.putInLocal(nameVariable, variable);
        }else{
            error.signal("Error: Variable already exists!");
        }
        
        //preeche a hashtable pela primeira vez
        if(lexer.token == Symbol.SEMICOLON){
            lexer.nextToken();
            return new VariableDecl(variable);
        }else{
            error.signal("Error: ';' expected!");
        }
        return null;
    }
    
    
    //Variable ::= Type Ident
    private Variable Variable(){
        Type type = Type();
        
        if(type == null){
            return null;
        }
        
        Ident ident = Ident();
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.MULT 
            || lexer.token == Symbol.DIV || lexer.token == Symbol.ASSIGN  
            || lexer.token == Symbol.PCT  || lexer.token == Symbol.HASHTAG 
            || lexer.token == Symbol.LT || lexer.token == Symbol.GT || lexer.token == Symbol.RIGHTBRACES 
            || lexer.token == Symbol.EM){
            error.signal("Error: Bad formation of variable!");
        }
        return new Variable (type, ident);
    }

    //Type ::= StdType | ArrayType
    private Type Type(){
        
        Symbol c = StdType();
        
        if(lexer.token == Symbol.LEFTBRACKETS){
            
            int number = ArrayType();
            return new Type(c, '1', number);//1 significa array
        }else if ( c == Symbol.ERROR){
            return null;
        }
        return new Type(c, '0');//0 significa normal
    }

    
    //StdType ::= ‘int’ | ‘double’ | ‘char’
    private Symbol StdType(){
        Symbol c = Symbol.ERROR; 
        
        if (lexer.token == Symbol.INT){
            c = lexer.token;
            lexer.nextToken();
        }else if (lexer.token == Symbol.DOUBLE){
            c = lexer.token;
            lexer.nextToken();
        }else if (lexer.token == Symbol.CHAR){
            c = lexer.token;
            lexer.nextToken();
        }
        return c;
	}

	//ArrayType::= StdType '[' ']'
    private int ArrayType(){
         
        if(lexer.token == Symbol.LEFTBRACKETS){
            lexer.nextToken();
           
            if(lexer.token == Symbol.NUMBER){
                int number = lexer.getNumberValue();
                lexer.nextToken();
                
                if(lexer.token == Symbol.DOT){
                    error.signal("Error: You can't declare vector with decimal!");
                }
                
                if(lexer.token == Symbol.RIGHTBRACKETS){
                    lexer.nextToken();
                    flagIntArray = 0;
                    return number;
                }else{
                    error.signal("Error: ] expected!");
                }
            }else{
                error.signal("Error: Declaration of int vector incorrect!");
            }
        }
        return 0;
    }


    //FunctionDecl ::= Type Ident ‘(’ Formals ‘)’ StmtBlock | ‘void’ Ident ‘(’ Formals ‘)’ StmtBlock
    private FunctionDecl FunctionDecl(){
        FunctionDecl function = null;
        StmtBlock stmtBlock = null;
        Formals formals = null;
        Ident ident = null;
        Type type = null;
        int flagMain = 0;
        
        //Funcao Void 
        if(lexer.token == Symbol.VOID){
            lexer.nextToken();
            if(lexer.token != Symbol.MAIN){
                ident = Ident();
                //Symbol s = Symbol.VOID;
                type = new Type(Symbol.VOID, '0');
                Object name = symbolTable.get(ident.getNameIdent());
                if(name != null){
                    error.signal("Error: Function already declared!");
                }
            }else{
                
                ident = new Ident("main");
                
                Object name = symbolTable.get(ident.getNameIdent());
                if(name != null){
                    error.signal("Error: Function already declared!");
                }
                
                flagMain = 1;
                lexer.nextToken();
            }
            
            
            
            if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                formals = Formals();//sao os parametros da função
                
                if(formals!= null && flagMain == 1){
                    error.signal("Error: Function MAIN must not have arguments!");
                }
                
                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                    
                    currentFunction = null; 
                    stmtBlock = StmtBlock();
                    
                    function = new FunctionDecl(type, ident, formals, stmtBlock);
                    
                    symbolTable.putInGlobal(ident.getNameIdent(), function);
                    
                       
                    return function;
                }else{
                    error.signal("Error: ( required !");
                }
            }else{
                error.signal("Error: ) required !");
            }
        }else{
            
            type = Type();
        
            ident = Ident();
            if(ident != null){
                
                if(type == null){
                    error.signal("Error: function declaration is wrong! The type is required!");
                }
               
                Object name = symbolTable.getInGlobal(ident.getNameIdent());
                if(name != null){
                    error.signal("Error: Function already declared!");
                }
                            
                if(lexer.token == Symbol.LEFTPAR){
                    lexer.nextToken();
                    formals = Formals();
                    if(lexer.token == Symbol.RIGHTPAR){
                        lexer.nextToken();
                        
                        currentFunction = type;
                        
                        stmtBlock = StmtBlock();
                        
                        function = new FunctionDecl(type, ident, formals, stmtBlock);
                        symbolTable.putInGlobal(ident.getNameIdent(), function);

                        return function;
                    }else{
                        error.signal("Error:");
                    }
                }else{
                    error.signal("Error:");
                }
            }else{
                if(type != null){
                    error.signal("Error: function declaration is wrong!");
                }
            }
        }
        
        return null;
    }
    
    //Formals ::= [ Variable { ‘,’ Variable } ]
    private Formals Formals(){
        ArrayList<Variable>variableList = new ArrayList<Variable>();
        Variable v_aux = null;
        
        Variable v = Variable();
        
        if(v != null){
            symbolTable.putInLocal(v.getVariableIdent().getNameIdent(), v);
            while(true){
                
                //Aqui nao pode haver variaveis de mesmo nome tambem, entao verificar.
                if(lexer.token == Symbol.COMMA){
                    lexer.nextToken();
                    v_aux = Variable();
                    if(v_aux == null){
                        error.signal("Error: You must declare a variable here after the , !");
                    }
                    variableList.add(v_aux);
                    
                    Object obj = symbolTable.getInLocal(v_aux.getVariableIdent().getNameIdent());
                    
                    if(obj != null){
                        error.signal("Error: Variable already declared!");
                    }
                    
                    symbolTable.putInLocal(v_aux.getVariableIdent().getNameIdent(), v_aux);
                }else{
                    break;
                }
            }
            return new Formals(v,variableList);
        }
        return null;
    }

	//Stmt ::= Expr ';' | ifStmt | WhileStmt | BreakStmt | PrintStmt | ReturnStmt
    private Stmt Stmt(){
        Expr expr = null;
        IfStmt ifStmt = null;
        WhileStmt whileStmt = null;
        PrintStmt printStmt = null;
        BreakStmt breakStmt = null;
        ReturnStmt returnStmt = null;
        
        if(lexer.token == Symbol.RIGHTBRACES){
            return null;
        }

        if(lexer.token == Symbol.IF){
        	ifStmt = IfStmt();           
        	return new Stmt(ifStmt);
        }else if(lexer.token == Symbol.WHILE){
        	whileStmt = WhileStmt();
        	return new Stmt(whileStmt);
        }else if(lexer.token == Symbol.BREAK){
        	breakStmt = BreakStmt();
        	return new Stmt(breakStmt);
        }else if(lexer.token == Symbol.PRINT){
        	printStmt = PrintStmt();
        	return new Stmt(printStmt);
        }else if(lexer.token == Symbol.RETURN){
            returnStmt = ReturnStmt();
            return new Stmt(returnStmt);
        }else{
        	expr = Expr();
        	if(expr!= null){
            	if(lexer.token == Symbol.SEMICOLON){
                    if(flagAtrib == 0 && flagOperator == 0 )
                        error.signal("Error: Invalid operation!");

               		lexer.nextToken();
                	return new Stmt(expr);
            	}else{
                    error.signal("Error: ; expected!");
                }
        	}
        }
        
        System.out.println("Sou null em stmt");
        return null;
	}

    //Expr ::= SimExpr [RelOp Expr]
    private Expr Expr(){
        Expr expr = null;
        Expr expr_aux = null;
        SimExpr simExpr = null;
        SimExpr simExpr_aux = null;
        
        simExpr=SimExpr();
        
        if(simExpr == null)
            return null;
        
        Type type = simExpr.getSimExprType();
        
        Symbol realOp = RelOp();//verifica =
        if (realOp != Symbol.ERROR){
            flagOperator = 1;
            expr_aux = Expr();
            if(expr_aux == null)
                error.signal("Error: After RelOp is required a Expr!");
            
            //Verificação dos tipos
            simExpr_aux = expr_aux.getExprSimExpr();
            Type type_aux = simExpr_aux.getSimExprType();
            
            //Tratamento dos numeros ou Expr
            if(type_aux.getNumero() == 1){//significa que eh um numero.
                if((type.getType().equals(Symbol.CHAR) && type_aux.getType().equals(Symbol.CHAR))){
                    return new Expr(simExpr, realOp, expr_aux, type);
                }else if((type.getType().equals(Symbol.INT) && type_aux.getType().equals(Symbol.INT))){
                    return new Expr(simExpr, realOp, expr_aux, type);
                }else if((type.getType().equals(Symbol.DOUBLE) && type_aux.getType().equals(Symbol.DOUBLE))){
                    return new Expr(simExpr, realOp, expr_aux, type);
                }else{
                    error.signal("Error: Number and String can't do this operation!");
                }
            }else{
                //trata se for Expr
                if(type.getType().equals(type_aux.getType()) && type.getTypeArray() == type_aux.getTypeArray()){
                    return new Expr(simExpr, realOp, expr_aux, type);
                }else{
                error.signal("Error: Expr - Differents types used!");
                }
            }
        }
        
        return new Expr(simExpr, type);
    }


    //SimExpr ::= [Unary] Term {AddOp Term}
    private SimExpr SimExpr(){
        int flagWhile = 0;
        Term term = null;
        Term term2 = null;
        ArrayList<Symbol> addOpList = new ArrayList<Symbol>();
        ArrayList<Term> termList = new ArrayList<Term>();
        Symbol unary = Symbol.ERROR;
        
        unary = Unary();
        term = Term();
        
        if(term == null)
            return null;
            
        Type type = term.getTermType();

        if(unary == Symbol.EM && type.getType().equals(Symbol.DOUBLE)){
            error.signal("Error: Operator! applied to Double. This behavior is undefined in GCC!");
        }

        while(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.MAGNITUDE){
            addOpList.add(AddOp());
            flagOperator = 1;
            term2 = Term();
            if(term2 == null)
                error.signal("Error: Term required after AddOP!");
            Type type_aux =  term2.getTermType();
            
            
            //Tratamento dos numeros
            if(type_aux.getNumero() == 1){//significa que eh um numero.
                if( !(type.getType().equals(Symbol.CHAR) && type_aux.getType().equals(Symbol.CHAR)) ){
                    termList.add(term2);
                    flagWhile = 1;
                }else{
                    error.signal("Error: Number and String can't do this operation!");
                }
            }else{
            
                if(type.getType().equals(type_aux.getType()) && type.getTypeArray() == type_aux.getTypeArray() ){
                    termList.add(term2);
                    flagWhile = 1;
                }else if(type.getType().equals(Symbol.INT) || type.getType().equals(Symbol.DOUBLE) || type_aux.getType().equals(Symbol.DOUBLE) || type_aux.getType().equals(Symbol.INT)){
                    termList.add(term2);
                    flagWhile = 1;
                }else{
                    error.signal("Error: Differents types used!");
                }
            }
        }
        
        
        if(unary != Symbol.ERROR && flagWhile == 0){
            return new SimExpr(unary, term,type);
        }else if(unary == Symbol.ERROR && flagWhile == 0){
            return new SimExpr(term,type);
        }else if(unary == Symbol.ERROR && flagWhile == 1){
            return new SimExpr(term, addOpList, termList,type);
        }else{
            return new SimExpr(unary, term, addOpList, termList,type);
        }
    }

    //Term ::= Factor { MulOp Factor}
    private Term Term(){
        int flagWhile = 0;
        Factor factor = null;
        Factor factor2 = null;
        ArrayList<Symbol> mulOpList = new ArrayList<Symbol>();
        ArrayList<Factor> factorList = new ArrayList<Factor>();
        Symbol currentSymbol;

        factor = Factor();
        if(factor == null)
            return null;
        Type type = factor.getFactorType();
        
        while (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV || lexer.token == Symbol.PCT){
            currentSymbol = lexer.token;
            flagOperator = 1;

            mulOpList.add(MulOp());
            factor2 = Factor();
            if(factor2 == null)
                error.signal("Error: Factor required after MulOp!");
            Type type_aux = factor2.getFactorType();
            

            if( currentSymbol == Symbol.PCT && type_aux.getType().equals(Symbol.DOUBLE)){
                error.signal(" Error: Operator % applied to Double, it's not possible to perform the operation!");
            }

            //Tratamento dos numeros ou Expr
            if(type_aux.getNumero() == 1){//significa que eh um numero.
                if(!(type.getType().equals(Symbol.CHAR) && type_aux.getType().equals(Symbol.CHAR)) ){
                    factorList.add(factor2);
                    flagWhile = 1;
                }else{
                    error.signal("Error: Number and String can't do this operation!");
                }
            }else{
                //trata se for Factor2
                if(type.getType().equals(type_aux.getType()) && type.getTypeArray() == type_aux.getTypeArray() ){
                    factorList.add(factor2);
                    flagWhile = 1;
                }else{
                    error.signal("Error: Differents types used!");
                }
            }
        }

        if(flagWhile == 0){
            return new Term(factor, type);
        }else{
            return new Term(factor, mulOpList, factorList, type);
        }
    }


    private Type functions(){
        Type type = new Type();
        
        //‘readInteger’ ‘(’ ‘)’   
        if(lexer.token == Symbol.READINTEGER){
            lexer.nextToken();
                if(lexer.token == Symbol.LEFTPAR){
                    lexer.nextToken();
                    if(lexer.token == Symbol.RIGHTPAR){
                        lexer.nextToken();
                            
                        type.setType(Symbol.INT);
                        return type;
                    }
                }
        //‘readDouble’ ‘(’ ‘)’ 
        }else if (lexer.token == Symbol.READDOUBLE){
            lexer.nextToken();
                if(lexer.token == Symbol.LEFTPAR){
                    lexer.nextToken();
                    if(lexer.token == Symbol.RIGHTPAR){
                            lexer.nextToken();

                            type.setType(Symbol.DOUBLE);
                            return type;
                    }
                }
                
        //‘readChar’ ‘(’ ‘)’ 
        }else if(lexer.token == Symbol.READCHAR){
            lexer.nextToken();
                if(lexer.token == Symbol.LEFTPAR){
                    lexer.nextToken();
                    if(lexer.token == Symbol.RIGHTPAR){
                        lexer.nextToken();
                            

                        type.setType(Symbol.CHAR);
                        return type;
                    }
            }
        }

        return null;
    }

    
    // Factor :: = LValue ‘:=’ Expr| LValue| Number| Call | ‘(’ Expr ‘)’ | ‘readInteger’ ‘(’ ‘)’ | ‘readDouble’ ‘(’ ‘)’ | ‘readChar’ ‘(’ ‘)’ <--------------refazer
    //CharConstant | StringConstant
    private Factor Factor(){
        LValue lValue = null;
        Expr expr = null;
        Numbers numbers = null;
        
        
        //caso uma funcao seja chamada antes de uma atribuicao, devera ocorrer uma falha
        //Type flag = functions();
            
        if(lexer.token == Symbol.READCHAR || lexer.token == Symbol.READDOUBLE || lexer.token == Symbol.READINTEGER ){
           error.signal("Error: Reading functions must be assignment expressions!");
        }
        
        //StringConstant
        if(lexer.token == Symbol.QMARK){
            lexer.nextToken();
            String message = " ";
            
            //concatena as palavras da mensagem
            while(lexer.token != Symbol.QMARK){
                String s = lexer.getNameVariable();
                message = message.concat(s);
                if(lexer.token != Symbol.IDIV)
                    message = message.concat(" ");
                lexer.nextToken();
                if(lexer.token == Symbol.EOF){
                    error.signal("Error: Bad formation of string!");
                }
            }
            lexer.nextToken();
            return new Factor(message);
        }
        
        //charConstant
        if(lexer.token == Symbol.IBAR){
            char c = lexer.getCharValue();
            if(lexer.token == Symbol.IBAR){
                lexer.nextToken();
                Type typeChar = new Type();
                typeChar.setType(Symbol.CHAR);
                return new Factor(c, typeChar);
            }else{
                error.signal("Error: CharConstant error!");
            }
        }

        lValue = LValue();
        
        if(lexer.token == Symbol.DQ){
            lexer.nextToken();
            flagAtrib = 1;
            
            //StringConstant
            if(lexer.token == Symbol.QMARK){
                lexer.nextToken();
                String message = " ";
                
                //concatena as palavras da mensagem
                while(lexer.token != Symbol.QMARK){
                    String s = lexer.getNameVariable();
                    message = message.concat(s);
                    if(lexer.token != Symbol.IDIV)
                        message = message.concat(" ");
                    lexer.nextToken();
                }
                
                //Aqui verifico se o Lvalue eh int, double ou char
                Ident ident = lValue.getIdent();//pega o ident referente ao lvalue
                String name = ident.getNameIdent();//pega a string referente ao nome do ident
                
                Object value = symbolTable.getInLocal(name);//busca na tabela de variaveis se existe
                
                if(value == null)
                    error.signal("Error: Variable Wasn't Declared!");
                
                Variable v = (Variable)value;
                Type type  = (Type)v.getVariableType();
                
                if(!(type.getType().equals(Symbol.CHAR))){
                    error.signal("Error: Atribuicao invalid between Char and Int/Double!");
                }
                
                System.out.println("Message "+message.length()+"Number vector "+type.getNumberVector());
                if(message.length()-1 > type.getNumberVector()){
                    error.signal("Error: String has more character then the vector permits");
                }
                
                
                lexer.nextToken();
                return new Factor(lValue,message);
            }
            
            //LValue := FUCTIONS
            Type typefunction  = functions();
            if(typefunction != null){

                //Aqui verifico se o Lvalue eh int, double ou char
                Ident ident = lValue.getIdent();//pega o ident referente ao lvalue
                String name = ident.getNameIdent();//pega a string referente ao nome do ident
                
                Object value = symbolTable.getInLocal(name);//busca na tabela de variaveis se existe
                
                if(value == null)
                    error.signal("Error: Variable Wasn't Declared!");
                Variable v = (Variable)value;
                Type type  = (Type)v.getVariableType();
                
                if((type.getType().equals(Symbol.CHAR) && typefunction.getType().equals(Symbol.CHAR)) ){
                    return new Factor(lValue, typefunction, 1);

                }else if((type.getType().equals(Symbol.INT) && typefunction.getType().equals(Symbol.INT))){
                    return new Factor(lValue, typefunction, 1);

                }else if((type.getType().equals(Symbol.DOUBLE) && typefunction.getType().equals(Symbol.DOUBLE))){
                    return new Factor(lValue, typefunction, 1);

                }else{
                    error.signal("Error: Function doesn't have the same type of atribuition!");
                }
            }

            //LValue ‘:=’ Letter
            if(lexer.token == Symbol.IBAR){
                char c = lexer.getCharValue();
                if(lexer.token == Symbol.IBAR){
                    lexer.nextToken();
                    
                    //Aqui verifico se o Lvalue eh int, double ou char
                    Ident ident = lValue.getIdent();//pega o ident referente ao lvalue
                    String name = ident.getNameIdent();//pega a string referente ao nome do ident
                
                    Object value  = symbolTable.getInLocal(name);//busca na tabela de variaveis se existe
                
                    if(value == null)
                        error.signal("Error: Variable wasn't declared!");
                    
                    Variable v = (Variable)value; 
                    Type type  = (Type)v.getVariableType();
                    
                    //A unica acao valida sera char := char;
                    if(type.getType().equals(Symbol.INT) || type.getType().equals(Symbol.DOUBLE)){
                        error.signal("Error: Comparation invalid between Char and Int/Double!");
                    }
                    //retorna o char
                    type.setType(Symbol.CHAR);
                    return new Factor(lValue, c, type);
                }else{
                    error.signal("Error: ' is Required to close the Char!");
                }
            }


            // lvalue := '('Expr')' 
            if(lexer.token == Symbol.LEFTPAR){
                
                lexer.nextToken();
                expr = Expr();
                if(expr != null){
                    if(lexer.token == Symbol.RIGHTPAR){
                        //VERIFICAR SE EH POSSIVEL (1+1)
                        Type type = expr.getExprType();
                        if(type.getNumero() == 1)
                            error.signal("Error: The compiler doesn't accept expr with only numbers!");
                        
                        lexer.nextToken();
                        return new Factor(lValue,expr,type, 1);
                    }
                }
            }
            
            expr = Expr();
            //Esse expr esta estritamente relacionado a atribuicao, por exemplo lvalue := expr
            if(expr != null){
                
                //verificar os dois tipos na atribuição
                Ident ident = lValue.getIdent();//pega o ident referente ao lvalue
                String name = ident.getNameIdent();//pega a string referente ao nome do ident
                
                Object value = symbolTable.getInLocal(name);//busca na tabela de variaveis se existe
                
                if(value == null)
                    error.signal("Error: Variable wasn't declared!");
                
                Variable v = (Variable)value;
                Type type  = (Type)v.getVariableType();
                
                Type type_aux = expr.getExprType();
                
                
                if(type_aux == null){
                    //fazer varios gets para descobrir o type do Call e comparar com o tipo de lValue!
                    //...
                }
                
                //Tratamento dos numeros
                if(type_aux.getNumero() == 1){//significa que eh um numero.
                
                    if( (type.getType().equals(Symbol.CHAR) && type_aux.getType().equals(Symbol.CHAR)) ){
                        
                        return new Factor(lValue,expr, type);
                    }else if((type.getType().equals(Symbol.INT) && type_aux.getType().equals(Symbol.INT))){
                        
                        return new Factor(lValue, expr,type);
                    }else if((type.getType().equals(Symbol.DOUBLE) && type_aux.getType().equals(Symbol.DOUBLE))){
                         
                         return new Factor(lValue,expr,type);
                    }else{
                        error.signal("Error: These types can't do this operation!");
                    }
                }
                // && type.getTypeArray() == type_aux.getTypeArray()

                if (type.getType().equals(type_aux.getType())){
                   
                    SimExpr sE = expr.getExprSimExpr();
                    Term t = sE.getTerm();
                    Factor f = t.getFactor();
                    LValue lV = f.getLValue();
                    String s = lV.getIdent().getNameIdent();
                    
                    Object o = symbolTable.getInLocal(s);
                    Variable variableExpr = (Variable)o;
                    Type typeArray  = (Type)variableExpr.getVariableType();
                    
                    
                    Expr e = lV.getExpr();
                    if(e == null && typeArray.getTypeArray() == '1'){
                        error.signal("Error: Bad atribuition!");
                        
                    
                    }
                    
                    return new Factor(lValue,expr, type);
                }else{
                    error.signal("Error: Different types compared!");
                }
                
            }else{
                error.signal("Error: Write an expr after := !");
            }
        }else{

            //CALLS!
            if(lValue!=null){
                Ident i = lValue.getIdent(); //pega o ident referente ao lvalue
            
                if(i!=null){
                    Object n = symbolTable.getInGlobal(i.getNameIdent());
                    if(n != null){
                        Calls calls = Calls(i);
                        if(calls!=null){
                            Object obj = symbolTable.get(i.getNameIdent());
                            
                            //VERIFICAR SE TEM OS MESMO TIPOS E QTD DE VARIAVEIS!!!!!
                            FunctionDecl func = (FunctionDecl)obj;
                            Formals formals = func.getFormals();// parametros da funcao
                            
                            Actuals actuals = calls.getActuals();// parametros da chamada da funcao
                            
                            //Funcao nao tem parametro mas a chamada de classe tem
                            if(formals == null && actuals != null){
                                error.signal("Error: Function doesnt have parameter!");
                            }else if(formals == null && actuals == null){//funcao e chamada não tem parametros
                                //tipo pode ser void, int, double ou char porem sem parametros de funcao
                                Type typeFunc = func.getType();
                                return new Factor(calls,typeFunc);
                            }else{
                                
                                //verificar a quantidade de parametro
                                Variable variable = formals.getVariable();
                                ArrayList<Variable> variableList = formals.getVariableList();
                                int num1 = variableList.size()+1;
                                
                                Expr exprCalls = actuals.getExpr();
                                ArrayList<Expr>exprList = actuals.getExprList();
                                
                                int num2 = exprList.size()+1;
                                
                                if(num1 != num2){
                                    error.signal("Error: Function and calling Fucntion dont have the same number of parameter!");
                                }else{
                                    
                                    //verificar os tipos dos parametros
                                    //cada expr tem um type então eh facil obter, formals tem variaveis que possuem types
                                    Type t = variable.getVariableType();
                                    Type t2 = exprCalls.getExprType();
                                    
                                    if(t.getType().toString() != t2.getType().toString()){
                                        error.signal("Error: Function and calling Fucntion dont have the same parameter type!");
                                    }else{
                                        
                                        //verificar para os lists ExprList e Variablist
                                        for (int x =0; x < variableList.size();x++){
                                            t =  variableList.get(x).getVariableType();
                                            t2 = exprList.get(x).getExprType();
                                            if(t.getType().toString() != t2.getType().toString()){
                                               error.signal("Error: Function and calling Fucntion dont have the same parameter type!");
                                            }
                                        }
                                        
                                    }
                                    
                                }
                                
                            }
                            
                            Type type = func.getType();
                            return new Factor(calls, type);
                        }
                    }else if(lexer.token == Symbol.LEFTPAR){
                        error.signal("Error: Function not declared!");
                    }
                }
            }
            
            // '('Expr')' 
            if(lexer.token == Symbol.LEFTPAR){
                System.out.println("Segundo (expr)");
                lexer.nextToken();
                expr = Expr();
                if(expr != null){
                    if(lexer.token == Symbol.RIGHTPAR){
                        //VERIFICAR SE EH POSSIVEL (1+1)
                        Type type = expr.getExprType();
                        if(type.getNumero() == 1)
                            error.signal("Error: The compiler doesn't accept expr with only numbers!");
                        
                        lexer.nextToken();
                        return new Factor(lValue,expr,type, 1);
                    }
                } 
            //Number
            }else{

                Numbers result = Numbers();
                
                if(result != null){
                //double res = result.doubleValue();
                    if(result.getType() == Symbol.INT){//Basta calcular o modulo para diferenciar se eh int ou double
                    //inteiro
                        Type type = new Type();
                        type.setNumero(1);//identifica que o type eh de um numero
                        type.setType(Symbol.INT);
                        return new Factor(result, type);
                    }else if(result.getType() == Symbol.DOUBLE){
                        //double
                        Type type = new Type();
                        type.setNumero(1);
                        type.setType(Symbol.DOUBLE);
                        return new Factor(result, type);
                    }
                }
                
                
                if(lValue != null){
                    Ident ident = lValue.getIdent();//pega o ident referente ao lvalue
                    String name = ident.getNameIdent();//pega a string referente ao nome do ident
                    
                    Object value = symbolTable.getInLocal(name);//busca na tabela de variaveis se existe
                    
                    if(value == null)
                        error.signal("Error: Variable not found and declared!");
                
                    Variable v = (Variable)value;
                    Type type  = (Type)v.getVariableType();
                    
                    return new Factor(lValue, type);
                }
            }
        }
        
        return null;
    } 
 

    //Calls ::= Ident ‘(’ Actuals ‘)’
    private Calls Calls(Ident ident ){
        
        Object name = symbolTable.getInGlobal(ident.getNameIdent());
        if(name == null){
                error.signal("Error: Fuction not declared");
        }
        
        if(lexer.token == Symbol.LEFTPAR){
            lexer.nextToken();
            
            Actuals a = Actuals();
            if(lexer.token == Symbol.RIGHTPAR){
                lexer.nextToken();
                return new Calls (ident, a);
            }
        }
        return null;
    }
    
    //Actuals ::= Expr { ‘,’ Expr }
    private Actuals Actuals(){
        ArrayList<Expr>exprList = new ArrayList<Expr>();
        
        Expr expr = Expr();
        
        if(expr == null){
            return null;
        }
        
        while(true){
            if(lexer.token == Symbol.COMMA){
                lexer.nextToken();
                Expr expr_aux = Expr();
                if(expr_aux == null){
                    error.signal("Error: You must declare a variable here after the , !");
                }
                exprList.add(expr_aux);
            }else{
                break;
            }
        }
        
        return new Actuals(expr, exprList);
    }
    
    //Number ::= IntNumber { ‘.’ IntNumber } <------BUGUEI
    private Numbers Numbers (){
        
        //Sei que eh um number eh agora?
        if (lexer.token == Symbol.NUMBER){
            int num = lexer.getNumberValue();
            
            lexer.nextToken();
            
            //Caso tenha ponto então tem a casa decimal, divido por 100 e somo depois
            if (lexer.token == Symbol.DOT){
                lexer.nextToken();
                if(lexer.token == Symbol.NUMBER){
                    double num2 = lexer.getNumberValue();
                    
                    //pega a quantidade de digit que tem em num2
                    //int length = (int)(Math.log10(num2)+1); //funciona pra numeros > 0
                    double length = num2==0?1:(1 + (int)Math.floor(Math.log10(Math.abs(num2))));//funciona para < ou > 0
                    //divide por 10 elevado ao numero de digito(length) para achar o decimal 
                    num2 = num2 / (Math.pow(10,length));
                    
                    //Soma dos dois, agora temos o double compreto.
                    double result = (num+num2);
                    
                    lexer.nextToken();
                    
                    return new Numbers(Symbol.DOUBLE, result);
                }else{
                    error.signal("Error: Invalid double number!");
                }
            }
            
            return new Numbers(Symbol.INT, num);
        }
        
        return null;
    }

    //LValue ::= Ident | Ident ‘[’ Expr ‘]’
    private LValue LValue(){
        Expr expr = null;
        Ident ident = null;
        ident = Ident();
        
        if(ident == null){
            return null;
        }
        
        if(lexer.token == Symbol.LEFTBRACKETS){
            lexer.nextToken();
            expr = Expr();
            if(expr == null)
                error.signal("Error: Expr needed here!");
            if(lexer.token == Symbol.RIGHTBRACKETS){
                lexer.nextToken();
                return new LValue(ident, expr);
            }else{
                error.signal("Error: LValue!");
            }
        }
        return new LValue(ident);//passar o Type do ident como parametro
    } 

	//ifStmt ::= ‘if’ ‘(’ Expr ‘)’ ‘{’ { Stmt } ‘}’ [ ‘else’ ‘{’ { Stmt } ‘}’ ]
    private  IfStmt IfStmt(){
        Expr expr = null;
        Stmt stmt = null;
        Stmt stmt2 = null;
        ArrayList<Stmt> stmtList = new ArrayList<Stmt>();
        ArrayList<Stmt> stmt2List = new ArrayList<Stmt>();
        int flagWhile = 0;
        int flagWhile2 = 0;

        if(lexer.token == Symbol.IF){
            lexer.nextToken();
            if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                expr = Expr();
                if(expr == null)
                    error.signal("Error: If must have an expression!");
                expr.setBloco(1);
                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                    if(lexer.token == Symbol.LEFTBRACES){
                        lexer.nextToken();

                        //laço do stmt '{'{Stmt}'}'
                        while(true){
                        	stmt = Stmt();
                        	if(stmt == null){
                        		break;
                        	}
                        	flagWhile = 1;
                        	stmtList.add(stmt);
                        }
                        if(lexer.token == Symbol.RIGHTBRACES){
                        	lexer.nextToken();
                        	
                        	//'else' '{' {Stmt} '}']
                        	if(lexer.token == Symbol.ELSE){
                        		lexer.nextToken();
                        		if(lexer.token == Symbol.LEFTBRACES){
                        			lexer.nextToken();
                        			while(true){
                        				stmt2 = Stmt();
                        				if(stmt2 == null){
                        					break;
                        				}
                        				flagWhile2 = 1;
                        				stmt2List.add(stmt2);
                        			}
                        			if(lexer.token == Symbol.RIGHTBRACES){
                                        lexer.nextToken();
                        			}else{
                                        error.signal("Error: '}' expected!!!");
                                    }
                        		}else{
                        		    error.signal("Error: '{' expected!!!");
                        		}
                        		
                        	}
                        	
                        	if(flagWhile == 1 && flagWhile2 == 1){
                        		return new IfStmt(expr, stmtList, stmt2List);
                        	}else if(flagWhile == 1 && flagWhile2 == 0){
                        		return new IfStmt(expr, stmtList);
                            }else if(flagWhile == 0 && flagWhile2 == 1){
                                return new IfStmt(expr,stmt2List);
                        	}else if(flagWhile == 0 && flagWhile2 == 0){
                        		return new IfStmt(expr);
                        	}else{
                        		error.signal("Error: Syntax error!");
                        	}
                        }
                    }
                }
            }
        }
        error.signal("Error: Syntax Error");
        return null;
	}

	//WhileStmt ::= ‘while’ ‘(’ Expr ‘)’ ‘{’ { Stmt } ‘}’
    private WhileStmt WhileStmt(){
        Expr expr = new Expr();
        Stmt stmt = null;
        ArrayList<Stmt> stmtList = new ArrayList<Stmt>();
        int flagWhile = 0;

        flagWhileBreak = 1;//indica que existe um bloco de while presente.
        if(lexer.token == Symbol.WHILE){
            lexer.nextToken();
            if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                //'('Expr ')
                expr = Expr();
                if(expr == null)
                    error.signal("Error: While needs an expression between () !");
                expr.setBloco(1);    
                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                    
                    //'{' {Stmt}'}'
                    if(lexer.token == Symbol.LEFTBRACES){
                        lexer.nextToken();
                       	while(true){
                       		stmt = Stmt();
                            if(stmt == null){
                                break;
                            }
                            flagWhile = 1;
                            stmtList.add(stmt);
                        }
                        
                        if(lexer.token == Symbol.RIGHTBRACES){
                            lexer.nextToken();
                            if(flagWhile == 1){
                            	return new WhileStmt(expr, stmtList);
                            }else if(flagWhile == 0){
                            	return new WhileStmt(expr);
                            }else{
                            	error.signal("Error: While syntax error!");
                            }
                        }
                    }
                }
            }
        }
        error.signal("Error: While syntax error!");
        return null;
	}

	//BreakStmt ::= ‘break’ ‘;’
    private BreakStmt BreakStmt(){
        
        if(flagWhileBreak == 0){
            error.signal("Error: Syntax error, Break must be inside of while block!");
        }else if(lexer.token == Symbol.BREAK){
            lexer.nextToken();
            if(lexer.token == Symbol.SEMICOLON){
                lexer.nextToken();
                flagWhileBreak = 0;
                return new BreakStmt();
            }
        }
        error.signal("Error: Break syntax error!");
        return null;

	}

	//PrintStmt ::= ‘print’ ‘(’ Expr { ‘,’ Expr }‘)’ ‘;’
    private PrintStmt PrintStmt(){
        Expr expr = new Expr();
        ArrayList<Expr> exprList = new ArrayList<Expr>();
        int flagWhile = 0; //tem a função de verificar se o {';'Expr} repete, caso repita recebe 1

        if(lexer.token == Symbol.PRINT){
            lexer.nextToken();
            if(lexer.token == Symbol.LEFTPAR){
                lexer.nextToken();
                expr = Expr();
                
                
                if(expr == null){
                    error.signal("Error: Print Syntax must have an expression between ( ) !");
                }
                
                
                //{ ‘,’ Expr }
                while(true){
                    if(lexer.token == Symbol.COMMA){
                        lexer.nextToken();
                        Expr expr_aux = Expr();
                        if(expr_aux != null){
                            exprList.add(expr_aux);
                            flagWhile = 1;
                        }else{
                            error.signal("Error: Print syntax must have an expression after , !");
                        }
                    }else{
                        break;
                    }
                }

                if(lexer.token == Symbol.RIGHTPAR){
                    lexer.nextToken();
                    if(lexer.token == Symbol.SEMICOLON){
                        lexer.nextToken();
                        if(flagWhile == 0){
                            return new PrintStmt(expr);
                        }else{
                            return new PrintStmt(expr,exprList);
                        }
                    }else{
                        error.signal("Error: ';' expected!");
                    }
                }else{
                    error.signal("Error: ')' expected!");
                }
            }else{
                error.signal("Error: '(' expected!");
            }
        }
        return null;
	}

    //ReturnStmt ::= ‘return’ [ Expr ] ‘;’
	private ReturnStmt ReturnStmt(){
	    Expr expr = null;
	    if(lexer.token == Symbol.RETURN){
	        lexer.nextToken();
	        
	        expr = Expr();
	        
	        //Verifica se type function eh igual expr fucntion 
	        if(currentFunction == null && expr != null){
	            error.signal("Error: Void function doesnt have return parameter!");
	            
	        }else if(currentFunction != null && expr != null){
	            Type exprType = expr.getExprType();
	            String s1 = currentFunction.getType().toString();
	            String s2 = exprType.getType().toString();
	            
	            if(s1 != s2){
	                error.signal("Error: Return and Function have different types!");
	            }
	            
	        }
	        
	        
	        
	        if(lexer.token == Symbol.SEMICOLON){
	            lexer.nextToken();
	            
	            return new ReturnStmt(expr);
	        }else{
	            error.signal("Error: Return needs a ;");
	        }
	    }
	    
	    return null;
	}
	
	//Verifica o token corrente
	private Boolean CheckOper(){
		if(lexer.token == Symbol.SEMICOLON || lexer.token == Symbol.PLUS 
            || lexer.token == Symbol.MINUS || lexer.token == Symbol.MULT 
            || lexer.token == Symbol.DIV || lexer.token == Symbol.ASSIGN  
            || lexer.token == Symbol.PCT  || lexer.token == Symbol.HASHTAG 
            || lexer.token == Symbol.LT || lexer.token == Symbol.GT || lexer.token == Symbol.RIGHTBRACES 
            || lexer.token == Symbol.EM || lexer.token == Symbol.RIGHTPAR)
			return true;
		else
			return false;
	}

	//Ident ::= Letter { ‘_’ | Letter | Digit }
    private Ident Ident(){
        Symbol c;
        Ident ident = null;

        nameVariable = lexer.getNameVariable();
        
        if(CheckOper())
        	return null;
        if(lexer.token == Symbol.IDENT){ // token agora pode vir como uma string
            nameVariable = lexer.getNameVariable();
            lexer.nextToken();
	        return new Ident(nameVariable);       
        }
        return null;
	}

	//RelOp ::= ‘=’ | ‘!=’ | ‘<’ | ‘<=’ | ‘>’ | ‘>=’
    private Symbol RelOp(){
		Symbol relOp = Symbol.ERROR;

    	if (lexer.token == Symbol.ASSIGN || lexer.token == Symbol.NEQ || lexer.token == Symbol.LT || lexer.token == Symbol.LE ||lexer.token == Symbol.GT || lexer.token == Symbol.GE){
    		relOp = lexer.token;
    		lexer.nextToken();
            return relOp;
    	}
    	return relOp;
	}

	//AddOp ::= ‘+’ | ‘-’ | '||'
    private Symbol AddOp(){
		Symbol addOp = Symbol.ERROR;

    	if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.MAGNITUDE){
    		addOp = lexer.token;
    		lexer.nextToken();
    	}
    	return addOp;
	}

	//MulOp ::= ‘*’ | ‘/’ | ‘%’ | '&&'
    private Symbol MulOp(){
		Symbol mulOp = Symbol.ERROR;

    	if (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV || lexer.token == Symbol.PCT || lexer.token == Symbol.DAMPERSAND){
    		mulOp = lexer.token;
    		lexer.nextToken();
            return mulOp;
    	}
    	return mulOp;
	}

	//Unary ::= ‘+’ | ‘-’ | ‘!’
    private Symbol Unary(){
    	Symbol unary = Symbol.ERROR;

    	if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.EM){
    		unary = lexer.token;
    		lexer.nextToken();
            return unary;
    	}
    	return unary;
		
	}

	//Digit ::= ‘0’ | ‘1’ | ... | ‘9’
    private Symbol Digit(){
        Symbol c = Symbol.ERROR;
    	if(lexer.token == Symbol.NUMBER){
            c = lexer.token;
    		lexer.nextToken();
            return c;
    	}

		return c;
	} 

	//Letter ::= ‘A’ | ‘B’ | ... | ‘Z’ | ‘a’ | ‘b’ | ... | ‘z’
    private Symbol Letter(){
    	Symbol letter = Symbol.ERROR;

    	if (lexer.token == Symbol.IDENT){
    		letter = lexer.token;
    		lexer.nextToken();
    	}

    	return letter;
	}    

    private Lexer lexer;
    private SymbolTable symbolTable; // variaveis criadas no sistema
    private CompilerError error;
    private String nameVariable;
    private int flagWhileBreak = 0;
    private int flagIntArray = 0;
    private int flagCharArray = 0;
    private int flagDoubleArray = 0;
    private int numberValue;

    private int flagAtrib = 0;
    private int flagOperator = 0;
    
    //nao aplicavel mais
    private int  tokenPos;
    private ArrayList<Variable> variableDeclList; 
    private Type currentFunction = null;
      
}