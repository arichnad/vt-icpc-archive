#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * Mike Heffner <mheffner@vt.edu>
 *
 * gcc -o convert convert.c
 */

struct unit {
	double val;
	char set;
};

struct unit map[512][512];

char units[512][128];

int min_a, min_b;
int max;

int GET_IDX(char *s)
{
	int _i;

	for (_i = 0; _i < max; _i++) {
		if (strcmp(units[_i], s) == 0)
			break;
	}
	return _i;
}

int main(int ac, char **av)
{
	char line[4096];
	int unit_a_idx, unit_b_idx;
	char *cp, *unit_a, *unit_b;
	double val, val_a, val_b, val_tmp;
	int idx, i, j, k, count, what;
	int to, start;
	long l;

	while (1) {
		fgets(line, sizeof(line), stdin);

		if (line[0] == '\n')
			break;


		memset(&map, 0, sizeof(map));
		memset(&units, 0, sizeof(units));

		for (i = 0; i < 512; i++)
			for (j = 0; j < 512; j++)
				map[i][j].set = 0;

		min_a = min_b = -1;
		max = 0;

		while (min_a == -1 || min_b == -1) {
			count = 0;
			cp = strtok(line, " \n");
			while (cp != NULL) {
				strcpy(units[max++], cp);
				cp = strtok(NULL, " \n");
				count++;
			}

			if (min_a == -1)
				min_a = max - 1;
			else
				min_b = max - 1;

			for (i = 1; i < count; i++) {
				fgets(line, sizeof(line), stdin);

				cp = strtok(line, " \n");
				sscanf(cp, "%lf", &val_a);

				unit_a = strtok(NULL, " \n");
				strtok(NULL, " \n");	/* = */

				cp = strtok(NULL, " \n");
				sscanf(cp, "%lf", &val_b);

				unit_b = strtok(NULL, " \n");

				unit_a_idx = unit_b_idx = -1;

				unit_a_idx = GET_IDX(unit_a);
				unit_b_idx = GET_IDX(unit_b);

				map[unit_a_idx][unit_b_idx].set = 1;
				map[unit_a_idx][unit_b_idx].val = val_b / val_a;
				map[unit_b_idx][unit_a_idx].set = 1;
				map[unit_b_idx][unit_a_idx].val = val_a / val_b;
			}
			fgets(line, sizeof(line), stdin);
		}

		/* Read connector! */
		cp = strtok(line, " \n");
		sscanf(cp, "%lf", &val_a);

		unit_a = strtok(NULL, " \n");

		strtok(NULL, " \n");

		cp = strtok(NULL, " \n");
		sscanf(cp, "%lf", &val_b);

		unit_b = strtok(NULL, " \n");

		unit_a_idx = GET_IDX(unit_a);
		unit_b_idx = GET_IDX(unit_b);

		map[unit_a_idx][unit_b_idx].set = 1;
		map[unit_a_idx][unit_b_idx].val = val_b / val_a;
		map[unit_b_idx][unit_a_idx].set = 1;
		map[unit_b_idx][unit_a_idx].val = val_a / val_b;

		for (i = 0; i < max; i++) {
			map[i][i].set = 1;
			map[i][i].val = 1.0;
		}

		for (i = 0; i < max; i++)
		 	for (j = 0; j < max; j++)
				if (!map[i][j].set)
					map[i][j].val = -1.0;

#if 0
		for (i = 0; i < max; i++) {
		 	for (j = 0; j < max; j++) {
				printf("[%.2d][%.2d]: %5.5lf ",
				    i, j, map[i][j].val);
			}
			printf("\n");
		}
#endif

		for (k = 0; k < max; k++)
			for (i = 0; i < max; i++)
				for (j = 0; j < max; j++) {
					if (!map[i][j].set &&
					    (map[i][k].set && map[k][j].set)) {
						map[i][j].set = 1;
						map[i][j].val =
						    map[i][k].val *
						    map[k][j].val;
					}
				}

#if 0
		for (i = 0; i < max; i++) {
		 	for (j = 0; j < max; j++) {
				printf("[%.2d][%.2d]: %5.5lf ",
				    i, j, map[i][j].val);
			}
			printf("\n");
		}
#endif

		fgets(line, sizeof(line), stdin);
		while (line[0] != '\n') {
			what = -1;
			val = 0.0;

			cp = strtok(line, " \n");
			while (cp != NULL) {
				sscanf(cp, "%lf", &val_tmp);

				cp = strtok(NULL, " \n");
				idx = GET_IDX(cp);

				if (what == -1)
					what = idx <= min_a ? min_a : min_b;

				val += val_tmp * map[idx][what].val;

				cp = strtok(NULL, " \n");
			}

			to = what == min_a ? min_b : min_a;

			val = val * map[what][to].val;

			start = to == min_a ? 0 : min_a + 1;

			for (i = start; i <= to; i++) {
				if (i == to) {
				  if (i != start)
				    printf(" ");
				  printf("%ld %s\n",
					 (long)(val + 0.5), units[to]);
				  break;
				}

				val_tmp = ((val / map[i][to].val));
				l = (long)val_tmp;

				if (i != start)
					printf(" ");
				printf("%ld %s", l, units[i]);

				val -= (double)l * map[i][to].val;
			}


			fgets(line, sizeof(line), stdin);
		}
	}

	return 0;
}
