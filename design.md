# Designbeskrivning
## Innehållsförteckning
1. Inledning
2. Utgångsläge
3. Designval
4. Lösning
5. Analys och konsekvenser

## 1. Inledning
Vi har arbetat med att refraktorera BookingService och dela upp innehållet i denna klass i fler klasser med mindre respektive ansvarsområden.

Vi har inte arbetat med att modifiera eller göra nya endpoints, ändra i strukturen av DTO eller modellklasser, eller refraktorerat ListingService och/eller UserService. Vi har bara jobbat med BookingService och Booking relaterade klasser. 

## 2. Utgångsläge
Innan refraktoreringen så hade BookingService väldigt många funktioner och bröt alltså starkt mot Single Repsponsitbility principen, vilket gjorde klassen svåröverskådlig.

Den hade en mängd längre funktioner som bröt mot Open/Close principen, vilket innebar att det var svårt att vidareutveckla eller lägga till funktionalitet runt dessa utan att behöva gå in och ändra mycket i metoderna. Detta gjorde att det var lätt att ”ha sönder” nuvarande funktionalitet av misstag när man försökte bygga ut booking-relaterade funktionaliteter.

Klassen var dessutom väldigt tätt knuten till andra klasser (d.v.s. mycket direkt kommunikation, och inte via interfaces eller dylik abstraktion), vilket också gör det svårare att vidareutveckla applikationen med ny funktionalitet, då man måste gå in och modifiera på väldigt många ställen om man t.ex. vill modifiera hur prissättning görs.

Då BookingService var alltså en stor, svåröverskådlig klass med en central roll i applikations funktionaliteten, vilkens struktur var riskfylld och opraktisk för framtida vidareutveckling av applikationen.

## 3. Designval
### 3.1. Designprinciper
Vi har implementerat följande designprinciper:
- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Dependency Inversion Principle (DIP)

SRP var väldigt central för refaktoreringen, då BookingServices tidigare hade väldigt många ansvar och arbetet vi gjort till stor del handlat om att delegera ut ansvar från BookingService till interfaces för att göra koden tydligare och lättare att underhålla/vidareutveckla. 

OCP har också varit viktig, då strukturen på BookingService och dess metoder även bröt mot denna i många fall. Tidigare skulle det varit komplicerat att t.ex. addera en in nya bookingStatus alternativ eller helgpriser, då allt kod utgick ifrån nuvarande funktionalitet och inte var särskillt atomär. Att följa OCP gör koden mer framtidssäker och öppen för vidareutveckling.

DIP är har varit viktig för att göra kopplingen mellan BookingService och de andra serviceklasserna lösare och därmed med flexibel och öppen för påbyggnad. I enlighet med denna princip så har vi jobbat för att introducera fler interfaces istället för att ha direktkontakt emellan t.ex. de BookingService och UserService.

### 3.2. Designmönster
Vi har implementerat följande designmönster:
- Strategy Pattern 
- State Pattern
- Factory Pattern(ska kolla på??)
- Template Pattern

Strategy Pattern används för att centralisera prislogik och öppna upp för att ha flera olika prisstrategier. Detta pattern gör det lätt att i framtiden lägga till fler prisstrategier för t.ex. olika säsonger eller helgdagar, vilket innan var svårt att göra utan att helt skriva om alla individuella metoder som rörde pris. Detta pattern (liksom många patterns) hjälpte oss att följa OCP.

State Pattern används för hantering av bokningstatus och göra denna mer säker och OCP-compliant. Genom att följa state pattern så kunde vi kapsla in de olika statusalternativen med sin egen logik och även öppna upp för att lägga till fler statusalternativ i framtiden.

Factory Pattern används för DTOConversion, d.v.s. att skapa Booking objekt från BookingRequest objekt och BookingResponse objekt från Booking objekt. Genom att använda factory pattern så separerar vi ut DTOConversion metoderna i sin egen klass och följer således SRP. Vi gör även skapander av dessa objekt mer felsäker genom att ha en dedikerad klass med robust logik. ***[fortsätt skriv här när vi läst på /kodat lite mer]***

Template Method Pattern används för att standardisera flöden i BookingService(eller i ett interface??).... ***[fortsätt skriv här när vi läst på /kodat lite mer]***

***[Lägg in nytt sekvensdiagram - beskriv att tidigare beteende som bara inkluderade att BookingService gjorde allt.]***

Lägg i

## 4. Lösning
Den nya BookingService klassen har endast en koordinering funktion och specifika ansvar har delegerats ut till andra klasser som kommunicerar med BookingService.
Följande delar av det nya systemet beskrivs mer i detalj i kommande undersektioner
- BookingService
- AuthenticationService och IdValidationService
- BookingTemplateMethods (Template Method Pattern)
- PriceStrategies (StrategyPattern)
- StatesBooking (State Pattern)
- DTOConversionService

### 4.1. BookingService
***[Kort om hur denna klass ser ut nu, vilka ansvar den har kvar osv.]***

### 4.2. AuthenticationService och IdValidationService
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.3. BookingTemplateMethods
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.4. PriceStrategies (StrategyPattern)
PriceStrategyService är ett FunctionalInterface som ska ansvara för kommunikation mellan BookingService och skapandet av ett pris.
Detta interface integrerar med alla typer av pris hanteringar och  
används för att skicka vidare ett bas objekt för att senare implementera korrekt strategi beroende på vilka vilkor som uppfyllts. 
För att implementera korrekt strategi behandlar PriceContext priset och kollar vilket vilkor som stämmer och sedan kallar på korrekt strategi med rätt kalkylering.
För tillfället existerar två pris strategier, Standard och Weekend men planen för framtiden är att lägga till Season. 

### 4.5. StatesBooking (State Pattern)
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.6. DTOConversionService
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

## 5. Analys och Konsekvenser
***[Denna  section ska innehålla:
Hur förändringen förbättrade koden? t.ex. har förändringen gjort koden enklare att förstå?
Har den gjort det enklare att underhålla koden?
Eventuella nackdelar t.ex. ökad komplexitet, fler klasser, etc. och varför ni tycker det är värt det. Ni kan ta stöd av UML diagram om kodexempel]***


