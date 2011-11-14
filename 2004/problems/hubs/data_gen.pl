#!/usr/bin/perl -w

use strict;

my $count = 20;

my $min_d = 1;
my $max_d = 1000;
my $min_n = 2;
my $max_n = 100;
my $min_m = 1;
my $max_m = 1000;

sub rand_int($$) {
  return $_[0] + int(rand($_[1] - $_[0] + 1));
}

sub rand_d() {
  return rand_int($min_d, $max_d);
}

sub rand_node($) {
  return rand_int(1, $_[0]);
}

my %pending_n = ();

print "$count\n";
for (my $c = 1; $c <= $count; ++$c) {
  my ($n, $a, $b, $d, $i);
  if (0 == keys %pending_n) {
    %pending_n = map {$_ => 1} $min_n..$max_n;
  }
  do { $n = rand_int($min_n, $max_n); } while not $pending_n{$n};
  delete $pending_n{$n};

  # cap max m to n..n^2
  my $m1 = $n - 1;
  my $m2 = $n * rand_int(1, $n);
  my $m = rand_int($m1 < $min_m ? $min_m : $m1, $m2 > $max_m ? $max_m : $m2);
  print "$n $m\n";

  # connect two random points
  $a = rand_node($n);
  do { $b = rand_node($n); } while $a == $b;
  $d = rand_d();
  print "$a $b $d\n";

  # repeatedly increase the core set
  my %disconnected = map {$_ => 1} 1..$n;
  delete $disconnected{$a};
  delete $disconnected{$b};
  for ($i = 2; $i < $n; ++$i) {
    do { $a = rand_node($n); } while $disconnected{$a};
    do { $b = rand_node($n); } while not $disconnected{$b};
    $d = rand_d();
    print "$a $b $d\n";
    delete $disconnected{$b};
  }

  # the rest of the links are random
  for ($i = $n; $i <= $m; ++$i) {
    $a = rand_node($n);
    $b = rand_node($n);
    $d = rand_d();
    print "$a $b $d\n";
  }
}
