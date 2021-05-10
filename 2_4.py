import matplotlib.pyplot as plt
import matplotlib
import sys


vMs = [i for i in range(5, 55, 5)]
for vM in vMs:
    fig = plt.figure(figsize=(15,10))
    ax1 = fig.add_subplot(111)
    # ax1.set_xlabel('Tiempo (10^-10 s)', fontsize=27)
    # ax1.set_ylabel('Distancia recorrida acumulada (10^-5 m)', fontsize=27)
    ax1.tick_params(axis='both', which='major', labelsize=20, width=2.5, length=10)
    with open(f'ej2_4/vM_{vM}.csv') as f:
        lines = f.readlines()
        y = [int(line) for line in lines]
    plt.pie(y)
    plt.legend( labels = [f'Izquierda - {y[0]}', f'Derecha - {y[1]}', f'Arriba - {y[2]}', f'Abajo - {y[3]}', f'Adentro - {y[4]}'], fontsize=27)

    fig1=plt.gcf()
    # plt.show()
    plt.draw()
    plt.savefig(f'ej2_4/pie_vM{vM}.png')
