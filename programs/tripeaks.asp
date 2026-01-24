carta_giocabile(ID) :- carta(ID, _, _, Pos), posizione(Pos, _, _, 0).

valore(1..13).
adiacente(X,Y):- valore(X), valore(Y), X = Y + 1.
adiacente(X,Y):- valore(X), valore(Y), X = Y - 1.
adiacente(1, 13).
adiacente(13, 1).

mossa_valida(ID, V) :-
    carta_giocabile(ID),
    carta(ID, V, _, _),
    carta_scarto(VS),
    adiacente(V, VS).

esiste_mossa :- mossa_valida(_, _).

{ azione_gioca(ID) : mossa_valida(ID, _) } = 1 :- esiste_mossa.
azione_pesca :- not esiste_mossa, puo_pescare.

% -------------------------------
% STEP 1: Relazioni di copertura
% -------------------------------
copre(Pos1, Pos2) :-
    posizione(Pos1, R1, C1, _),
    posizione(Pos2, R2, C2, _),
    R1 = R2 + 1,
    C1 >= C2 - 1,
    C1 <= C2 + 1.

carta_copre(ID1, ID2) :-
    carta(ID1, _, _, Pos1),
    carta(ID2, _, _, Pos2),
    copre(Pos1, Pos2).

% -------------------------------
% STEP 2: Scarto dopo la mossa
% -------------------------------
scarto_dopo(ID_giocata, V) :- mossa_valida(ID_giocata, V).

% -------------------------------
% STEP 3: Mosse successive (versione ottimizzata)
% -------------------------------

% Una mossa ha un seguito se dopo averla giocata:
% - Esiste una carta che era già giocabile (e non è quella giocata)
% - E il suo valore è adiacente al nuovo scarto
mossa_dopo(ID_giocata) :-
    scarto_dopo(ID_giocata, VScarto),
    carta_giocabile(ID2),
    ID2 != ID_giocata,
    carta(ID2, V2, _, _),
    adiacente(V2, VScarto).

% -------------------------------
% STEP 4: Mosse sicure
% -------------------------------
mossa_sicura(ID) :-
    mossa_valida(ID, _),
    mossa_dopo(ID).

esiste_mossa_sicura :- mossa_sicura(_).

:~ azione_gioca(ID), not mossa_sicura(ID), esiste_mossa_sicura. [1@1, ID]

#show azione_gioca/1.
#show azione_pesca/0.
#show mossa_dopo/1.  % DEBUG
#show mossa_sicura/1.  % DEBUG

