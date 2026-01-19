# IT Help Desk sistem – Dokumentacija dizajnerskih i implementacionih odluka

## Uvod

Ovaj dokument detaljno opisuje arhitektonske i implementacione odluke donete tokom razvoja REST API serverske aplikacije za IT Help Desk sistem, čija je svrha evidencija i obrada servisnih zahteva IT podrške u okviru jedne organizacije.

Aplikacija je realizovana kao slojevita Java REST API aplikacija, bez front-end dela. Sve funkcionalnosti su demonstrirane putem Postman REST poziva.

## Funkcionalni zahtevi i implementacija

Svi funkcionalni zahtevi iz postavke zadatka su implementirani.

### Korisnici

#### Pregled liste korisnika
- GET /api/users
- Implementirano u UserRest klasi.
- Vraća listu svih korisnika.

#### Pregled liste korisnika
- GET /api/users/{id}
- Implementirano u UserRest klasi.
- Vraća listu svih korisnika.

#### Registracija korisnika
- POST /api/auth/register
- Implementirano u AuthRest klasi.
- Kreira novog korisnika sa osnovnim podacima (ime, prezime, username, email, lozinka, uloga).

#### Logovanje korisnika
- POST /api/auth/login
- Implementirano u AuthRest klasi.
- Proverava kredencijale i omogućava autentifikaciju korisnika.

### Servisni zahtevi (Tickets)

#### Kreiranje servisnog zahteva
- POST /api/tickets
- Implementirano u TicketRest, metod za kreiranje zahteva.
- Kreira novi ticket sa naslovom, opisom, kategorijom, prioritetom i autorom.

#### Pregled liste servisnih zahteva
- GET /api/tickets
- Implementirano u TicketRest.
- Vraća listu svih zahteva.

#### Filtriranje zahteva
- GET /api/tickets/filter
- Implementirano u TicketDAO i TicketService.
- Omogućava filtriranje po statusu, kategoriji, prioritetu, ključnim rečima, opsegu datuma.

#### Detaljan prikaz servisnog zahteva
- GET /api/tickets/{id}
- Implementirano u TicketRest.
- Vraća sve podatke o jednom ticket-u.

### Izmena zahteva

#### Izmena zahteva
- PUT /api/tickets/{id}
- Implementirano u TicketRest.
- Omogućava izmenu bilo kojeg polja servisnog zahteva.

#### Izmena statusa zahteva
- PUT /api/tickets/{id}/status
- Implementirano u TicketRest.
- Prilikom izmene statusa ažurira se status u tabeli tickets, automatski se kreira zapis u tabeli status_logs.

#### Izmena prioriteta zahteva
- PUT /api/tickets/{id}/priority
- Implementirano u TicketRest.
- Omogućava izmenu prioriteta zahteva.

#### Dodela zahteva IT radniku
- PATCH /api/tickets/{id}/assign
- Implementirano u TicketRest.
- Dodeljuje ticket određenom korisniku (IT radniku).

### Komentari i istorija promena

#### Dodavanje komentara na zahtev
- POST /api/comments
- Implementirano u CommentRest.
- Dodaje komentar za određeni ticket.

#### Pregled svih komentara
- GET /api/comments
- Implementirano u CommentRest.
- Vraća listu svih komentara.

#### Pregled istorije promena statusa
- GET /api/tickets/{id}/logs
- Implementirano u TicketRest.
- Vraća kompletnu istoriju promena statusa zahteva.

#### Pregled zahteva dodeljenih korisniku
- GET /api/users/{id}/assignedTickets
- Implementirano u UserRest.
- Vraća sve tickete koji su dodeljeni određenom IT radniku.

## Arhitektura i dizajn sistema

Sistem je razvijen kao REST API serverska aplikacija, koristeći troslojnu arhitekturu.

### REST sloj

Paketi: org.example.rest

Klase:
AuthRest, UserRest, TicketRest, CommentRest, StatusLogRest

### Service sloj

Paketi: org.example.service

Sadrži poslovnu logiku sistema, koordinira operacije nad više DAO klasa, implementira pravila sistema

Klase:
UserService, TicketService, CommentService, StatusLogService

### DAO sloj

Paketi: org.example.dao

Direktna komunikacija sa bazom podataka, izvršavanje SQL upita korišćenjem JDBC-a, mapiranje ResultSet objekata u model klase

Klase:
UserDAO, TicketDAO, CommentDAO, StatusLogDAO

### Model sloj

Paketi: org.example.model

Predstavlja domenske entitete:
User, Ticket, Comment, StatusLog

Korišćene su enumeracije:
TicketStatus, TicketPriority, Role

## Dizajnerske odluke i ispravke specifikacije
### Ispravke modela baze podataka

#### Standardizacija naziva

Sva imena tabela i polja su preimenovana na isti jezik i u skladu sa uobičajenim konvencijama imenovanja u bazama podataka

Primeri standardizovanih naziva: created_at, assigned_to, changed_at

Na ovaj način postignuta je doslednost u imenovanju i olakšano dalje proširivanje sistema.

#### Definisanje jasnih relacija

Relacije između ključnih entiteta su jasno definisane putem stranih ključeva:

tickets.created_by → users.id
(korisnik koji je kreirao servisni zahtev)

tickets.assigned_to → users.id
(IT radnik kome je zahtev dodeljen)

Ovakav pristup eliminiše nejasnoće iz originalnog modela i omogućava pouzdano povezivanje podataka unutar sistema.

#### Status i prioritet zahteva

Polja status i priority nisu modelovana kao posebne tabele u bazi, već su definisana kao ENUM vrednosti na nivou aplikacije.
Ova odluka doneta je zbog jednostavnije validacije podataka, smanjenja složenosti baze, jasne kontrole dozvoljenih vrednosti u poslovnoj logici
Promene statusa zahteva dodatno se evidentiraju kroz posebnu tabelu za istoriju promena (status log), čime se obezbeđuje potpuna sledljivost izmena.

#### Uloga korisnika 

U tabeli users, uloga korisnika je definisana ENUM poljem role. 
Na ovaj način jasno su razdvojene odgovornosti korisnika u sistemu.

### Ispravke REST API poziva

Originalni API pozivi nisu bili u potpunosti konzistentni.

POST /api/login - /api/auth/login : Putanja je izmenjena kako bi se jasno izdvojila funkcionalnost autentifikacije od ostalih korisničkih resursa.  
POST /api/users: Nepromenjeno.
GET /api/ticket - /api/tickets: Naziv resursa je promenjen u množinu zbog konzistentnosti sa REST konvencijama, gde kolekcije resursa koriste množinu.
GET /api/tickets/{id}: Nepromenjeno.
PUT /api/tickets/status/{id} - /api/tickets/{id}/status: Identifikator zahteva je premešten uz resurs, čime je putanja postala jasnija.
Status i ID korisnika koji vrši promenu prosleđuju se kao query parametri.
PATCH /api/ticket/{id}/assign - /api/tickets/{id}/assign: Naziv resursa je standardizovan kao tickets.
ID korisnika kojem je dodeljen zahtev se prosleđuje kao querz paramentar.
POST /api/comments: Nepromenjeno.
GET /api/tickets/{id}/logs: Nepromenjeno.
GET /api/users/{id}/assignedTickets: Nepromenjeno.

Ove izmene poboljšavaju čitljivost i konzistentnost API-ja.

## Upravljanje podacima i validacija

### Validacija podataka

Osnovne validacije (postojanje entiteta, validni ENUM statusi) vrše se u servisnom sloju.

### Konzistentnost podataka

Prilikom izmene statusa ažurira se ticket i dodaje se novi status log čime se obezbeđuje doslednost sistema.

### Bezbednost

Lozinke se ne čuvaju u otvorenom obliku već se obrađuju pre skladištenja u bazu.

## Testiranje sistema

Sve funkcionalnosti su testirane korišćenjem Postman alata.

Kreirana je Postman kolekcija sa primerima:
- registracije i logovanja,
- CRUD operacija nad zahtevima,
- komentara,
- status log-ova,
- filtriranja.

## Zaključak

Razvijena REST API aplikacija u potpunosti ispunjava sve zahteve zadatka IT Help Desk sistem.
Nejasnoće i nedoslednosti iz specifikacije su identifikovane, ispravljene i obrazložene.
Slojevita arhitektura omogućava dobru modularnost, lako održavanje, jasno razdvajanje odgovornosti.

Aplikacija predstavlja stabilnu osnovu za dalji razvoj i eventualno dodavanje front-end klijenta.
