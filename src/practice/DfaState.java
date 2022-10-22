package practice;

/**
 * 有限状态机的各种状态
 * 确定型有限自动机 Deterministic Finite Automata DFA
 */
public enum DfaState {
    Initial,

    IntLiteral,

    Id,
    Id_i, Id_in,Id_int,Int,
    Id_if,If,
    Else,Id_e,Id_el,Id_els,Id_else4,

    GT,GE,EQ,LT,LE,

    Plus,Minus,Star,Slash,

    Assignment,

    Semicolon, LeftParen, RightParen

}
