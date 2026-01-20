% ===================================================================
% REGOLE TRIPEAKS - Versione garantita funzionante per DLV2
% ===================================================================

% Una carta è giocabile se la sua posizione non è coperta
carta_giocabile(ID) :- carta(ID, _, _, Pos), posizione(Pos, _, _, 09).

% Definisce quando due valori sono adiacenti (distanza 1)

valore(1..13).
adiacente(X,Y):- valore(X), valore(Y), X = Y + 1.
adiacente(X,Y):- valore(X), valore(Y), X = Y - 1.

% Caso speciale: Re (13) e Asso (1) sono adiacenti
adiacente(1, 13).
adiacente(13, 1).

% Una mossa è valida se la carta è giocabile e il suo valore è adiacente alla carta scarto
mossa_valida(ID, V) :-
    carta_giocabile(ID),
    carta(ID, V, _, _),
    carta_scarto(VS),
    adiacente(V, VS).