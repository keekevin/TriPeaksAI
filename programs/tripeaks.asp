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

%esiste almeno una mossa valida in questo turno
esiste_mossa :- mossa_valida(_, _).

%se esistono mosse valide, ne sceglie esattamente una
{ azione_gioca(ID) : mossa_valida(ID, _) } = 1 :- esiste_mossa.

%se non esistono mosse valide e il mazzo non è vuoto, pesca una carta
azione_pesca :- not esiste_mossa, puo_pescare.

% -------------------------------
% Relazioni di copertura
% -------------------------------
%Pos2 è bloccata finché Pos1 non viene rimossa
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

%se gioco ID_giovata il nuovo valore in cima allo scarto sarà V
scarto_dopo(ID_giocata, V) :- mossa_valida(ID_giocata, V).


%dopo aver giocato ID_giocata, esiste almeno un'altra carta già scoperta e giocabile nel turno successivo
mossa_dopo(ID_giocata) :-
    scarto_dopo(ID_giocata, VScarto),
    carta_giocabile(ID2),
    ID2 != ID_giocata,
    carta(ID2, V2, _, _),
    adiacente(V2, VScarto).

% -------------------------------
% Lookahead profondità 2
% -------------------------------

%ID2 è giocabile come seconda mossa dopo aver giocato ID1
seconda_mossa(ID1, ID2) :-
    scarto_dopo(ID1, VScarto1),
    carta_giocabile(ID2),
    ID2 != ID1,
    carta(ID2, V2, _, _),
    adiacente(V2, VScarto1).

%dopo la sequenza ID1 -> ID2, il nuovo valore in cima allo scarto sara V2
scarto_dopo_2(ID1, ID2, V2) :-
    seconda_mossa(ID1, ID2),
    carta(ID2, V2, _, _).


%ID3 è una carta gia scoperta sul tavolo e giocabile come terza mossa
disponibile_dopo_2(ID1, ID2, ID3) :-
    seconda_mossa(ID1, ID2),
    carta_giocabile(ID3),
    ID3 != ID1,
    ID3 != ID2.


%la sequenza è prolungabile se esiste un ID£ il cui valore è adiacente al nuovo scarto V2
mossa_dopo_2(ID1, ID2) :-
    scarto_dopo_2(ID1, ID2, V2),
    disponibile_dopo_2(ID1, ID2, ID3),
    carta(ID3, V3, _, _),
    adiacente(V3, V2).

% -------------------------------
% Classificazione mosse
% -------------------------------

%garantisce almeno una mossa successiva
mossa_sicura(ID) :-
    mossa_valida(ID, _),
    mossa_dopo(ID).


%garantisce una sequenza di almeno 3 mosse
mossa_sicura_2(ID) :-
    mossa_valida(ID, _),
    mossa_dopo_2(ID, _).

%esiste almeno una mossa sicura al livello 1
esiste_mossa_sicura :- mossa_sicura(_).

%esiste almeno una mossa sicura al livello 2
esiste_sicura_2 :- mossa_sicura_2(_).

%esiste almeno una mossa che scopre carte
esiste_mossa_che_scopre :- scopre_carta(_).

% -------------------------------
% Ottimizzazione multilivello
% -------------------------------
% PRIORITÀ @3 - Lookahead 2
%penalizza le mosse che non garantiscono 3 mosse consecutive
:~ azione_gioca(ID), not mossa_sicura_2(ID), esiste_sicura_2. [3@3, ID]

% PRIORITÀ @2 - Scoprire carte
%penalizza le mosse che non liberano le carte bloccate
:~ azione_gioca(ID), not scopre_carta(ID), esiste_mossa_che_scopre. [2@2, ID]

% PRIORITÀ @1 - Lookahead 1
%penalizza le mosse che non garantiscono almeno una mossa successiva
:~ azione_gioca(ID), not mossa_sicura(ID), esiste_mossa_sicura. [1@1, ID]

% PRIORITÀ @0 - Tiebreaker valore
%sceglie la mossa con il valore piu basso
:~ azione_gioca(ID), carta(ID, V, _, _). [V@0, ID]


% -------------------------------
% Output
% -------------------------------
#show azione_gioca/1.
#show azione_pesca/0.
#show mossa_sicura/1.
#show mossa_sicura_2/1.

#show mossa_valida/2.
#show carta_giocabile/1.