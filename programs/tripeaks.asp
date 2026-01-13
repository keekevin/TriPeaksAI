% ===================================================================
% REGOLE TRIPEAKS - Versione garantita funzionante per DLV2
% ===================================================================

% Una carta è giocabile se la sua posizione non è coperta
carta_giocabile(ID) :- carta(ID, _, _, Pos), posizione(Pos, _, _, "false").

% Definisce quando due valori sono adiacenti (distanza 1)
adiacente(1, 2).
adiacente(2, 1).
adiacente(2, 3).
adiacente(3, 2).
adiacente(3, 4).
adiacente(4, 3).
adiacente(4, 5).
adiacente(5, 4).
adiacente(5, 6).
adiacente(6, 5).
adiacente(6, 7).
adiacente(7, 6).
adiacente(7, 8).
adiacente(8, 7).
adiacente(8, 9).
adiacente(9, 8).
adiacente(9, 10).
adiacente(10, 9).
adiacente(10, 11).
adiacente(11, 10).
adiacente(11, 12).
adiacente(12, 11).
adiacente(12, 13).
adiacente(13, 12).

% Caso speciale: Re (13) e Asso (1) sono adiacenti
adiacente(1, 13).
adiacente(13, 1).

% Una mossa è valida se la carta è giocabile e il suo valore è adiacente alla carta scarto
mossa_valida(ID, V) :-
    carta_giocabile(ID),
    carta(ID, V, _, _),
    carta_scarto(VS),
    adiacente(V, VS).