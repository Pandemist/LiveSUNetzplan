#Live S+U Netzplan

## Idee
Erstellung von Netzplänen (experimentell für Berlin) welche aktuellen Störungen (Langfristige: Baustellen, Ersatzverkehre & Kurzfristige: Umregelmäßigkeiten, Feuerwehr-/ Polizeieinsätze, etc.) in Echtzeit in den Netzplan Einarbeitet.\
Eine Beispielhafte Ausgabe für die Störungsmeldung: "Es kommt zwischen Friedrichstraße und Alexanderplatz wengen eines Feuerwehreinsatzes zu Verzögungerungen." liegt hier: [Klick mich!](https://github.com/Pandemist/LiveSUNetzplan/blob/main/res/Update.svg)

## Umsetzung
Das Programm spannt intern den Netzplan aus den Quelldaten auf. Anschließend werden die Bezugsquellen für Störungen und Baustelleninfos durchsucht um diese in eine Interne repräsentation zu vereinheitlichen. Im nächsten Schritt werden die erfassten Störungen auf den Netzplan angewandt. Im letzten Schritt wird auf Basis der Quelldaten der grafische Netzplan mit eingearbeiteten Strörungen erzeugt.

## Quelldaten
- Als Grundlage für den Netzplan wird eine [SVG-Grafik](https://github.com/Pandemist/LiveSUNetzplan/blob/main/res/Berlin.svg) verwendet, in welcher die einzelnen Elemente die benötigten Metadaten enthalten um intern das Netzwerk zu Simulieren
- Für anstehenden Störungen werden die Webseiten der Verkehrsbetriebe (BVG.de und S-Bahn.Berlin) sowie deren Infokanäle auf Twitter (x.com) verwendet.

## Aktuelle Probleme
- Die Ermittlung von Pfadverläufen, welche aus mehreren Bézierkurven bestehen ist aktuell Fehlerhaft.
- Seit der Anpassung der Twitter-Schnittstelle, ist die Abfrage dieser nicht mehr möglich.
- Wenn Störungsmeldugnen fehlerhaft sind (Rechtschreibfehler, verkürzte Bezeichnungen, Missachtung des Formats etc.) kann eine Korrekte Auswertung nicht mehr erfolgen.
- Leichte Darstellungsprobleme bei Mehrzeiligen Stationsnamen im Netzplan.
