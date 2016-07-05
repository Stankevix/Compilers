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


package AuxComp;
import AST.*;
import Lexer.*;
import java.util.ArrayList;
import java.util.*;

public class SymbolTable {
    
    public SymbolTable() {
        globalTable = new Hashtable<String, Object>();
        localTable  = new Hashtable<String, Object>();
    }
    
    public Object putInGlobal( String key, Object value ) {
       return globalTable.put(key, value);
    }

    public Object putInLocal( String key, Object value ) {
       return localTable.put(key, value);
    }
    
    public Object getInLocal( Object key ) {
       return localTable.get(key);
    }
    
    public Object getInGlobal( Object key ) {
       return globalTable.get(key);
    }
    
    public Object get( String key ) {
        // returns the object corresponding to the key. 
        Object result;
        if ( (result = localTable.get(key)) != null ) {
              // found local identifier
            return result;
        }
        else {
              // global identifier, if it is in globalTable
            return globalTable.get(key);
        }
    }

    public void removeLocalIdent() {
           // remove all local identifiers from the table
         localTable.clear();
    }
      
        
    public Hashtable<String, Object> globalTable;
    public Hashtable<String, Object> localTable;
}
            