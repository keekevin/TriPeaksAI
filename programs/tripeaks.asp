% ===================================================================
% REGOLE TRIPEAKS - Versione garantita funzionante per DLV2
% ===================================================================

% Una carta è giocabile se la sua posizione non è coperta
carta_giocabile(ID) :- carta(ID, _, _, Pos), posizione(Pos, _, _, 0).

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
% esiste almeno una mossa valida
esiste_mossa :- mossa_valida(_, _).

% -------------------------------
% SCELTA DELL'AZIONE
% -------------------------------

% Scelta: per ogni mossa valida, può essere giocata o no
azione_gioca(ID) | non_gioca(ID) :- mossa_valida(ID, _).

% VINCOLO: se esiste una mossa, devo giocarne ESATTAMENTE UNA
:- esiste_mossa, #count{ID : azione_gioca(ID)} != 1.

% Se NON esiste una mossa e posso pescare → pesca
azione_pesca :- not esiste_mossa, puo_pescare.

% Non posso fare entrambe le cose
:- azione_gioca(_), azione_pesca.

% -------------------------------
% OUTPUT
% -------------------------------
#show azione_gioca/1.
#show azione_pesca/0.