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
% Relazioni di copertura
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
% Lookahead profondità 1
% -------------------------------
scarto_dopo(ID_giocata, V) :- mossa_valida(ID_giocata, V).

mossa_dopo(ID_giocata) :-
    scarto_dopo(ID_giocata, VScarto),
    carta_giocabile(ID2),
    ID2 != ID_giocata,
    carta(ID2, V2, _, _),
    adiacente(V2, VScarto).

% -------------------------------
% Lookahead profondità 2
% -------------------------------
seconda_mossa(ID1, ID2) :-
    scarto_dopo(ID1, VScarto1),
    carta_giocabile(ID2),
    ID2 != ID1,
    carta(ID2, V2, _, _),
    adiacente(V2, VScarto1).

scarto_dopo_2(ID1, ID2, V2) :-
    seconda_mossa(ID1, ID2),
    carta(ID2, V2, _, _).

disponibile_dopo_2(ID1, ID2, ID3) :-
    seconda_mossa(ID1, ID2),
    carta_giocabile(ID3),
    ID3 != ID1,
    ID3 != ID2.

mossa_dopo_2(ID1, ID2) :-
    scarto_dopo_2(ID1, ID2, V2),
    disponibile_dopo_2(ID1, ID2, ID3),
    carta(ID3, V3, _, _),
    adiacente(V3, V2).

ha_sequenza_2(ID1) :- mossa_dopo_2(ID1, _).

% -------------------------------
% Classificazione mosse
% -------------------------------
mossa_sicura(ID) :-
    mossa_valida(ID, _),
    mossa_dopo(ID).

mossa_sicura_2(ID) :-
    mossa_valida(ID, _),
    ha_sequenza_2(ID).

esiste_mossa_sicura :- mossa_sicura(_).
esiste_sicura_2 :- mossa_sicura_2(_).

% -------------------------------
% Ottimizzazione multilivello
% -------------------------------
:~ azione_gioca(ID), not mossa_sicura_2(ID), esiste_sicura_2. [2@1, ID]
:~ azione_gioca(ID), not mossa_sicura(ID), esiste_mossa_sicura. [1@2, ID]

% -------------------------------
% DEBUG: Mostra penalizzazioni
% -------------------------------
penalizzata_livello_2(ID) :-
    azione_gioca(ID),
    not mossa_sicura_2(ID),
    esiste_sicura_2.

penalizzata_livello_1(ID) :-
    azione_gioca(ID),
    not mossa_sicura(ID),
    esiste_mossa_sicura.

mossa_ottimale(ID) :-
    azione_gioca(ID),
    not penalizzata_livello_2(ID),
    not penalizzata_livello_1(ID).

% -------------------------------
% Output
% -------------------------------
#show azione_gioca/1.
#show azione_pesca/0.
#show mossa_sicura/1.           % DEBUG
#show mossa_sicura_2/1.         % DEBUG
#show penalizzata_livello_1/1.  % DEBUG
#show penalizzata_livello_2/1.  % DEBUG
#show mossa_ottimale/1.         % DEBUG