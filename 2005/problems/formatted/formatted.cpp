/*************************************************************
**                                                          **
**  Sample solution for "Formatted Substring"               **
**       Steven J Zeil                                      **
**       10/25/2005                                         **
**                                                          **
*************************************************************/


#include <iostream>
#include <string>
#include <vector>


using namespace std;


string stripText (string str)
{
  string result;
  bool inTag = false;
  for (int i = 0; i < str.length(); ++i)
    {
      char c = str[i];
      if (c == '<')
	{
	  inTag = true;
	  result += c;
	}
      else if (c == '>')
	{
	  if (inTag)
	    {
	      inTag = false;
	      result += c;
	    }
	}
      else if (inTag)
	result += c;
    }
  return result;
}


string stripTag (string str)
{
  // Search str for a <tag></tag> pair with no intervening
  // text or elements and remove them from the string.

  string::size_type pos = 0;
  string::size_type oldPos = 0;
  while ((pos = str.find('<',oldPos)) != string::npos)
    {
      cerr << "tag at position " << pos;
      string::size_type endTagPos = str.find('>',pos);
      string tagName = str.substr(pos+1, endTagPos-pos-1);
      cerr << ", tag name is " << tagName << endl;
      string closingTag = "</" + tagName + ">";
      if (str.substr(endTagPos+1, closingTag.length()) == closingTag)
	return str.substr(0,pos) 
	  + str.substr(endTagPos+1+closingTag.length());
      else
	oldPos = pos+1;
    }
  return str;
}

string stripTags (string str)
{
  // Remove all empty <tag></tag> pairs from str
  string result = "";
  while (str != result)
    {
      result = str;
      str = stripTag(result);
    }
  result = str;
  return result;  
}


int main()
{
  int b, e;
  string text;

  while ((cin >> b >> e) && b >= 0) {
    getline (cin, text);
    // clip initial character (should be a blank)
    text = text.substr(1);

    vector<string> stack;

    // Remove all non-tag characters outside positions b..e
    string part1 = text.substr(0, b);
    string part2 = text.substr(b, e-b);

    string tag;
    bool inTag = false;
    for (int i = 0; i < part1.length(); ++i)
      {
	char c = part1[i];
	if (c == '<')
	  {
	    tag = '<';
	    inTag = true;
	  }
	else if (c == '>' && inTag) 
	  {
	    tag += c;
	    inTag = false;
	    if (tag[1] == '/')
	      stack.pop_back();
	    else
	      stack.push_back(tag);
	  }
	else if (inTag) {
	  tag += c;
	}
      }

    for (int i = 0; i < stack.size(); ++i)
      cout << stack[i];

    cout << part2;

    for (int i = 0; i < part2.length(); ++i)
      {
	char c = part2[i];
	if (c == '<')
	  {
	    tag = '<';
	    inTag = true;
	  }
	else if (c == '>' && inTag) 
	  {
	    tag += c;
	    inTag = false;
	    if (tag[1] == '/')
	      stack.pop_back();
	    else
	      stack.push_back(tag);
	  }
	else if (inTag) {
	  tag += c;
	}
      }

    while (!stack.empty())
      {
	cout << "</" << stack.back().substr(1);
	stack.pop_back();
      }
    cout << endl;
  }

  return 0;
}
