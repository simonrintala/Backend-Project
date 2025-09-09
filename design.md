# Designbeskrivning
## Innehållsförteckning
1. Inledning
2. Utgångsläge
3. Designval
4. Lösning
5. Analys och konsekvenser

## 1. Inledning
Vi har arbetat med att refraktorera BookingService och dela upp innehållet i denna klass i fler klasser med mindre respektive ansvarsområden. 
I refraktoreringen har vi delat upp BookingService i AuthenticationService, BookingValidationService, PriceCalculationService, DateAvailabilityService (***OBS! denna kanske vi inte gör, ta bort i så fall***) och DTOConversionService. Vi har utvecklat alla dessa med lämpliga designmönster och behållit endast koordinering av de nya klasserna i BookingService klassen.

Vi har inte arbetat med att modifiera eller göra nya endpoints, ändra i strukturen av DTO eller modellklasser, eller refraktorerat ListingService och/eller UserService. Vi har bara jobbat med BookingService och Booking relaterade klasser. 

## 2. Utgångsläge
Innan refraktoreringen så hade BookingService väldigt många funktioner och bröt alltså starkt mot Single Repsponsitbility principen, vilket gjorde klassen svåröverskådlig.

Den hade en mängd längre funktioner som bröt mot Open and Close principen, vilket innebar att det var svårt att vidareutveckla eller lägga till funktionalitet runt dessa utan att behöva gå in och ändra mycket i metoderna. Detta gjorde att det var lätt att ”ha sönder” nuvarande funktionalitet av misstag när man försökte bygga ut booking-relaterade funktionaliteter.

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
- Factory Pattern
- Template Pattern
- Command Pattern

Strategy Pattern används för att centralisera prislogik och öppna upp för att ha flera olika prisstrategier. Detta pattern gör det lätt att i framtiden lägga till fler prisstrategier för t.ex. olika säsonger eller helgdagar, vilket innan var svårt att göra utan att helt skriva om alla individuella metoder som rörde pris. Detta pattern (liksom många patterns) hjälpte oss att följa OCP.

State Pattern används för hantering av bokningstatus och göra denna mer säker och OCP-compliant. Genom att följa state pattern så kunde vi kapsla in de olika statusalternativen med sin egen logik och även öppna upp för att lägga till fler statusalternativ i framtiden.

Factory Pattern används för DTOConversion, d.v.s. att skapa Booking objekt från BookingRequest objekt och BookingResponse objekt från Booking objekt. Genom att använda factory pattern så separerar vi ut DTOConversion metoderna i sin egen klass och följer således SRP. Vi gör även skapander av dessa objekt mer felsäker genom att ha en dedikerad klass med robust logik. 

Template Method Pattern används för att standardisera flöden i BookingService(eller i ett interface??).... ***[fortsätt skriv här när vi läst på /kodat lite mer]***

Command Pattern används i BookingService för att för komplexa booking-operationer.... ***[fortsätt skriv här när vi läst på /kodat lite mer]***

***[Står i uppgiftsbeskrivningen att vi ska koppla dessa resonemang till UML, visa t.ex. klassdiagram före/efter eller sekvensdiagram för nytt beteende, så vi ska lägga in det här senare]***

## 4. Lösning
I den nya designen finns följande klasser:
- BookingService
- AuthenticationService
- BookingValidationService
- PriceCalculationService
- DateAvailabilityService ***(OBS! denna kanske vi inte gör, ta bort i så fall)***
- DTOConversionService

### 4.1. BookingService
***[Kort om hur denna klass ser ut nu, vilka ansvar den har kvar osv.]***

### 4.2. AuthenticationService
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.3. BookingValidationService
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.4. PriceCalculationService
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.5. DateAvailabilityService (***OBS! denna kanske vi inte gör, ta bort i så fall***)
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

### 4.6. DTOConversionService
***[Kort om hur denna klass ser ut, vilka ansvar den har och hur den interagerar med andra klasser.]***

## 5. Analys och Konsekvenser
***[Denna  section ska innehålla:
Hur förändringen förbättrade koden? t.ex. har förändringen gjort koden enklare att förstå?
Har den gjort det enklare att underhålla koden?
Eventuella nackdelar t.ex. ökad komplexitet, fler klasser, etc. och varför ni tycker det är värt det. Ni kan ta stöd av UML diagram om kodexempel]***


