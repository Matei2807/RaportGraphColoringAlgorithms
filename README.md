**Authors:** Hutu Matei-Alexandru, Plesa Marian-Cosmin, Tascu Stelian-Andrei
# Graph Coloring

Acest proiect implementează și evaluează algoritmi pentru colorarea grafurilor folosind următoarele metode:
- **Backtracking** (metodă exactă)
- **Greedy** (bazat pe gradul nodurilor)
- **Greedy DSATUR** (bazat pe gradul de saturație al nodurilor)

Proiectul include generatoare de teste, evaluări ale performanței algoritmilor și un set de grafuri predefinite, cat si rezultatele testelor efectuate.

---

## Structura proiectului

```
.
├── GraphColoring.java          # Codul principal al algoritmilor
├── PlotGenerator               # Scripturi pentru generarea graficelor
│   ├── regresionNodes.m        # Script MATLAB pentru analiză
│   └── regresionNodes.py       # Script Python pentru grafică
├── README.md                   # Documentația proiectului
├── Results                     # Rezultatele testelor
│   ├── avg_results.txt         # Rezultatele medii a fiecărui test dupa 50 de rulări
│   ├── summary_results.txt     # Rezumatul tuturor testelor, cat si a fiecarui din cele 50 de rulări
│   ├── test_result_X.txt       # Fișiere individuale cu rezultate pentru fiecare rulare din cele 50
├── TestGenerators              # Generatoare pentru grafuri de test
│   ├── BipartiteGraphsGenerator.java
│   ├── DenseGraphsGenerator.java
│   ├── MediumGraphsGenerator.java
│   ├── MultiComponentGraphsGenerator.java
│   ├── PlanarGraphsGenerator.java
│   └── generateSmall.py        # Am inceput initial generarea in python, dar am continuat in java(pentru a eficientiza procesul)
├── Teste_NrDeNoduri            # Teste grafuri cu densitate medie pentru 3-32 noduri
├── Tests                       # Grafuri predefinite pentru evaluare
```
---

## Conținutul directorului `TestGenerators`

Acest director conține scripturi pentru generarea diferitelor tipuri de grafuri:
- **BipartiteGraphsGenerator.java**: Creează grafuri bipartite.
- **DenseGraphsGenerator.java**: Creează grafuri dense.
- **MediumGraphsGenerator.java**: Creează grafuri de dimensiuni medii.
- **MultiComponentGraphsGenerator.java**: Creează grafuri cu componente multiple.
- **PlanarGraphsGenerator.java**: Creează grafuri planare.
- **generateSmall.py**: Script Python pentru generarea grafurilor mici.

---

## Cum se pot evalua soluțiile

1. **Compilarea și rularea codului:**
    - Rularea codului se face prin intermediul fișierului `GraphColoring.java`. Acesta conține implementările algoritmilor și metodele de evaluare
    - Implementarea se afla in main
    - Pentru rulare se poate folosi comanda `java GraphColoring` in terminal, sau `run` in acelasi fisier in IDE-ul folosit

2. **Rezultatele evaluării:**
    - Rezultatele sunt salvate automat în directorul `Results`

---

## Sursele soluțiilor

1. **Algoritmii Greedy și Backtracking:**
    - O parte din implementările algoritmilor au fost inspirate din articole disponibile pe GeekforGeeks

2. **Generatoarele de teste:**
    - Generatoarele pentru grafuri au fost dezvoltate cu ajutorul unor soluții AI pentru eficientizarea procesului de codare, cu mici modificări ulterioare

3. **Scripturi de analiză:**
    - Scripturile MATLAB și Python pentru grafică și analiză au fost dezvoltate pentru vizualizarea rezultatelor experimentelor(pentru generarea graficelor)

---

## Note suplimentare

- Testele includ 50 de rulări pentru fiecare graf, pentru a asigura consistența rezultatelor.
- Algoritmii Greedy sunt rulatii de 100 de ori(per test) pentru a realiza o medie a timpilor de rulare si a rezultatelor obtinute(algoritmul de backtracking este rulat doar o singura data, datorita faptului ca nu sunt necesare mai multe rulari)
- Toti timpii de execuție sunt exprimați în milisecunde, cu excepția cazurilor unde se specifică altfel.
- Exista un numar de teste care dau rezulate diferite fata de ceeea ce ne asteptam(timpi foarte mici sau foarte mari), acestea fiind rezultatul unor erori de implementare sau a unor cazuri particulare care nu sunt acoperite de algoritmii implementati.
- Din cauza timpului de rulare foarte mare pentru algoritmul de backtracking, am limitat numarul de noduri pentru care se poate rula la 32(in afara de grafurile bipartite, care se pot rula mult mai rapid).

