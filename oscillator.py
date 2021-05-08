import matplotlib.pyplot as plt
import matplotlib
import sys

# if len(sys.argv) <= 1:
#     print("Please pass the temperature as parameter. Exiting..")
#     sys.exit(-1)
paths = []
# TODO: agregar los otros 3 (beeman, verlet, etc)
with open('tp4/oscillator.csv') as f:
    lines = f.readlines()
    x = [float(line.split(' ')[0]) for line in lines]
paths = {'x': x}

fig = plt.figure(figsize=(15,10))
ax1 = fig.add_subplot(111)
# ax1.set_xlabel('Porcentaje de celulas vivas', fontsize=27)
# ax1.set_ylabel('Pasos hasta el corte', fontsize=27)
ax1.tick_params(axis='both', which='major', labelsize=20, width=2.5, length=10)

ax1.plot(paths['x'])
print(len(paths['x']))

# ax1.set_aspect( 1 )
# plt.xlim([0, 6])
# plt.ylim([0, 6])

fig1=plt.gcf()
plt.show()
plt.draw()
# plt.savefig(f'oscillator.png')
