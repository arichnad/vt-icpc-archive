#!/usr/bin/perl -w
$_ = <>;
m/^\s*0\s+0\s+2\s*$/ or
  m/^\s*2\s+0\s+0\s*$/ or
  die "Incorrect result on test case 1\n";
$_ = <>;
m/^\s*4\s+4\s+4\s+0\s+0\s+5\s+5\s*$/ or
  die "Incorrect result on test case 2\n";
$_ = <>;
m/^\s*8\s+8\s+8\s+9\s+9\s+9\s+8\s+0\s+0\s+9\s+8\s+8\s+8\s+9\s+9\s+9\s*$/ or
  die "Incorrect result on test case 3\n";
<> and die "Unexpected data after last test case";
0;
