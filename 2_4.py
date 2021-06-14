import matplotlib.pyplot as plt

fig = plt.figure(figsize=(15,10))
ax1 = fig.add_subplot(111)
# ax1.set_xlabel('Tiempo (10^-10 s)', fontsize=27)
# ax1.set_ylabel('Distancia recorrida acumulada (10^-5 m)', fontsize=27)
ax1.tick_params(axis='both', which='major', labelsize=20, width=2.5, length=10)

# vMs = [5, 10, 15, 20, 25, 30, 35, 40, 45, 50]
vMs = [100, 150, 200, 250, 300, 350, 400, 450, 500, 550]
vMs_labels = ['1x10⁴', '1.5x10⁴', '2x10⁴', '2.5x10⁴', '3x10⁴', '3.5x10⁴', '4x10⁴', '4.5x10⁴', '4x10⁴', '5.5x10⁴']
ys = []
for i in [0,3,6,9]:
    vM = vMs[i]
    with open(f'ej2_4/vM_{vM}.csv') as f:
        lines = f.readlines()
        y = [int(line) for line in lines]
    ax1.scatter(['Izquierda','Derecha','Arriba','Abajo','Adentro'], y, label=f'vx0={vMs_labels[i]}m/s', alpha=0.8)
    # ys.append(y)

plt.legend(loc='best', fontsize=18)
fig1=plt.gcf()

plt.show()
# plt.savefig(f'ej2_4/pie_vM{vM}.png')
