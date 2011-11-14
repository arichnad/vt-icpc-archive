/*************************************************************
**                                                          **
**  Sample solution for "Mobiles"                           **
**       Steven J Zeil                                      **
**       10/28/2005                                         **
**                                                          **
*************************************************************/


#include <iomanip>
#include <iostream>
#include <string>
#include <sstream>
#include <vector>


using namespace std;


struct Tree {
  int elementNum;
  double weight;
  double width;

  Tree* parent;
  Tree* left;
  Tree* right;

  Tree (int num)
    : elementNum(num), weight(0.0), width(0.0), 
      parent(0), left(0), right(0)
    {}

  double totalWeight () const;
  double totalWidth () const;

  bool isBalanced() const;
  bool swingsFreely() const;

};

double Tree::totalWeight () const
{
  if (left != 0 && right != 0)
    return left->totalWeight() + right->totalWeight();
  else 
    return weight;
}


double Tree::totalWidth () const
{
  if (left != 0 && right != 0)
    return width + left->totalWidth()/2.0 + right->totalWidth() / 2.0;
  else 
    return 0.0;
}


bool Tree::isBalanced () const
{
  if (left != 0 && right != 0)
    return left->isBalanced() && right->isBalanced() 
      && left->totalWeight() == right->totalWeight();
  else
    return true;
}

bool Tree::swingsFreely () const
{
  if (left != 0 && right != 0)
    return width > left->totalWidth()/2.0 + right->totalWidth()/2.0;
  else
    return true;
}





void reserveTreeNodes (vector<Tree*>& nodes, int k)
{
  while (nodes.size() < k)
    nodes.push_back (new Tree(nodes.size()));
}


struct Range {
  double left;
  double right;

  Range (double l = 0.0, double r = 0.0): left(l), right(r) {}
};


class Overlap {
  Range r;
public:
  Overlap (Range range): r(range) {}

  bool operator() (Range r1)
  {
    return (r1.left <= r.left && r1.right >= r.right)
      || (r.left <= r1.left && r.right >= r1.right)
      || (r.left <= r1.left && r.right >= r1.left)
      || (r.left <= r1.right && r.right >= r1.right);
  }
};


typedef vector<vector<Range> > RangeTable;


void buildRangeTable (Tree* t, double center, 
		      int level, RangeTable& levels)
{
  if (t != 0)
    {
      if (levels.size() <= level)
	levels.resize(level+1);
      
      Range r (center - t->width/2.0, center + t->width/2.0);
      levels[level].push_back (r);
      buildRangeTable (t->left, center - t->width/2.0, level+1, levels);
      buildRangeTable (t->right, center + t->width/2.0, level+1, levels);
    }
}

bool checkRangeTable (RangeTable& levels)
{
  for (int i = 0; i < levels.size(); ++i)
    {
      vector<Range>& level = levels[i];
      for (int j = 0; j+1 < level.size(); ++j)
	if (find_if(level.begin()+j+1, level.end(), Overlap(level[j]))
	    != level.end())
	  {
	    cerr << "Overlap in level " << i << " on range " << j << endl;
	    return false;
	  }
    }
  return true;
}


int main()
{
  int num = 0;
  vector<Tree*> nodes;
  int unknownWeightNode;

  while (num >= 0) {
    nodes.clear();

    cin >> num;
    while (num > 0) {
      reserveTreeNodes (nodes, num);

      double weight, width;
      int left, right;
      char c;
      
      cin >> c;
      if (c == 'D')
	{
	  string weightOrX;
	  cin >> weightOrX;
	  if (weightOrX == "X")
	    {
	      unknownWeightNode = num;
	    }
	  else
	    {
	      istringstream in (weightOrX);
	      in >> weight;
	      nodes[num-1]->weight = weight;
	    }
	}
      else
	{
	  cin >> width >> left >> right;
	  reserveTreeNodes (nodes, left);
	  reserveTreeNodes (nodes, right);
	  nodes[num-1]->width = width;
	  nodes[num-1]->left = nodes[left-1];
	  nodes[num-1]->right = nodes[right-1];
	  nodes[left-1]->parent = nodes[num-1];
	  nodes[right-1]->parent = nodes[num-1];
	}

      cin >> num;
    }

    // Part 1: what is the root of this tree?
    Tree* root = 0;
    int rootNum = 0;
    for (int i = 0; (i < nodes.size()) && (root == 0); ++i)
      {
	if (nodes[i]->parent == 0)
	  {
	    rootNum = i;
	    root = nodes[rootNum];
	  }
      }


    // Part 2 - try to balance the mobile
    double w1 = nodes[unknownWeightNode-1]->totalWeight();

    double requiredWeight = 
      nodes[unknownWeightNode-1]->parent->totalWeight() - w1;

    nodes[unknownWeightNode-1]->weight = requiredWeight;


    // Part 3 - check to see if it's balanced overall;
    bool balanced = (requiredWeight >= 0.0);
    balanced = balanced && root->isBalanced();

    // Part 4 - check to see if it swings freely
    bool swings = balanced;

    if (balanced) 
      {
	RangeTable levels;
	buildRangeTable (root, 0.0, 0, levels);
	for (int i = 0; i < levels.size(); ++i)
	  {
	    cerr << i << ": ";
	    for (int j = 0; j < levels[i].size(); ++j)
	      cerr << "(" << levels[i][j].left << ","
		   << levels[i][j].right << ") ";
	    cerr << endl;
	  }
	swings = checkRangeTable (levels);
      }

    if (balanced) 
      {
	cout << "Object " << unknownWeightNode
	     << " must have weight " << fixed << setprecision(2) 
	     << requiredWeight
	     << endl;
	if (swings)
	  cout << "The mobile will swing freely." << endl;
	else
	  cout << "The mobile will not swing freely." << endl;
      }
    else
      {
	cout << "The mobile cannot be balanced." << endl;
      }
    
  }

  return 0;
}
