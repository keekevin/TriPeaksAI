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
% Pos1 copre Pos2 se Pos2 richiede che Pos1 sia libera
copre(Pos1, Pos2) :- richiede_libera(Pos2, Pos1).

carta_copre(ID1, ID2) :-
    carta(ID1, _, _, Pos1),
    carta(ID2, _, _, Pos2),
    copre(Pos1, Pos2).

% -------------------------------
% Carte scoperte
% -------------------------------
ha_altra_copertura(ID_scoperta, ID_mossa) :-
    carta_copre(ID_mossa, ID_scoperta),
    carta_copre(ID_altro, ID_scoperta),
    ID_altro != ID_mossa.

% Una mossa scopre almeno una carta se copre qualcosa senza altre coperture
scopre_carta(ID_mossa) :-
    mossa_valida(ID_mossa, _),
    carta_copre(ID_mossa, ID_scoperta),
    not ha_altra_copertura(ID_scoperta, ID_mossa).

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

% Flag: esiste almeno una mossa che scopre carte
esiste_mossa_che_scopre :- scopre_carta(_).

% -------------------------------
% Ottimizzazione multilivello
% -------------------------------
% PRIORITÀ @3 - Lookahead 2
:~ azione_gioca(ID), not mossa_sicura_2(ID), esiste_sicura_2. [3@3, ID]

% PRIORITÀ @2 - Scoprire carte
:~ azione_gioca(ID), not scopre_carta(ID), esiste_mossa_che_scopre. [2@2, ID]

% PRIORITÀ @1 - Lookahead 1
:~ azione_gioca(ID), not mossa_sicura(ID), esiste_mossa_sicura. [1@1, ID]

% PRIORITÀ @0 - Tiebreaker valore
:~ azione_gioca(ID), carta(ID, V, _, _). [V@0, ID]

% -------------------------------
% DEBUG
% -------------------------------
%penalizzata_livello_2(ID) :-
%    azione_gioca(ID),
%    not mossa_sicura_2(ID),
%    esiste_sicura_2.

%penalizzata_livello_1(ID) :-
%    azione_gioca(ID),
%    not mossa_sicura(ID),
%    esiste_mossa_sicura.

%mossa_ottimale(ID) :-
%    azione_gioca(ID),
%    not penalizzata_livello_2(ID),
%    not penalizzata_livello_1(ID).

% -------------------------------
% Output
% -------------------------------
#show azione_gioca/1.
#show azione_pesca/0.
#show mossa_sicura/1.
#show mossa_sicura_2/1.

#show mossa_valida/2.
#show carta_giocabile/1.