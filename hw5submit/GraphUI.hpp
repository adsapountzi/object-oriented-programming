
#ifndef _GRAPH_UI_
#define _GRAPH_UI_
#include <iostream>
#include <bits/stdc++.h>
template <typename T>
int graphUI() {
  
  string option, line;
  int distance;
  bool digraph = false;
  
  cin >> option;
  if(!option.compare("digraph"))
    digraph = true;
  Graph<T> g(digraph);
  
  while(true) {
    
    std::stringstream stream;
    cin >> option;
    
    if(!option.compare("av")) {
      getline(std::cin, line);
      stream << line;
      T vtx(stream);
      if(g.addVtx(vtx))
        cout << "av " << vtx << " OK\n";
      else
        cout << "av " << vtx << " NOK\n";
    }
    else if(!option.compare("rv")) {
        getline(std::cin, line);
            stream << line;
            T vtx(stream);
            if (g.rmvVtx(vtx))
                cout << "rv " << vtx << " OK\n";
            else
                cout << "rv " << vtx << " NOK\n";
        }
    
    else if(!option.compare("ae")) {
        getline(std::cin, line);
            stream << line;
            T from(stream);
            T to(stream);
            stream >> distance;
            if (g.addEdg(from, to, distance))
                cout << "ae " << from << " " << to << " OK\n";
            else
                cout << "ae " << from << " " << to << " NOK\n";
      
    }
    else if(!option.compare("re")) {
        getline(std::cin, line);
            stream << line;
            T from(stream);
            T to(stream);
            if (g.rmvEdg(from, to))
                cout << "re " << from << " " << to << " OK\n";
            else
                cout << "re " << from << " " << to << " NOK\n";

    }
    else if(!option.compare("dot")) {
        getline(std::cin, line);
        stream << line;
        const char* filename = line.c_str();
        if (g.print2DotFile(filename))
            cout << "dot " << filename << " OK\n";
        else
            cout << "dot " << filename << " NOK\n";
    }
    else if(!option.compare("bfs")) {
        getline(std::cin, line);
        stream << line;
        T node(stream);
        cout << "\n----- BFS Traversal -----\n";
        list<T> bf = g.bfs(node);
        if (!bf.empty()) {
            typename list<T>::iterator it = bf.begin();
            cout << *it;
            ++it;
            for (; it != bf.end(); ++it)
                cout << " -> " << *it;
        }
        cout << "\n-------------------------\n";
    }
    else if(!option.compare("dfs")) {
        getline(std::cin, line);
        stream << line;
        T node(stream);
        
        cout << "\n----- DFS Traversal -----\n";
        list<T> df = g.dfs(node);
        if (!df.empty()) {
            typename list<T>::iterator it = df.begin();
            cout << *it;
            ++it;
            for (; it != df.end(); ++it)
                cout << " -> " << *it;
        }
      cout << "\n-------------------------\n";
    }
    else if(!option.compare("dijkstra")) {
        getline(std::cin, line);
        stream << line;
        T from(stream);
        T to(stream);

        cout << "Dijkstra (" << from << " - " << to <<"): ";
    
        list<T> result = g.dijkstra(from, to);

        auto it = result.cbegin();
        cout << *it;

        it++;
        for (*it; it != result.cend(); ++it){
		    std::cout << ", " << *it;
        }
        std::cout << endl;
    }
    else if(!option.compare("mst")) {

        
      std::cout << "\n--- Min Spanning Tree ---\n";

      list<Edge<T>> result = g.mst();
      for(Edge<T> n : result){
        std::cout << n << endl;
      }
      std::cout << "MST Cost: " << g.sum << endl;
    }
    else if(!option.compare("q")) {
      cerr << "bye bye...\n";
      return 0;
    }
    else if(!option.compare("#")) {
      string line;
      getline(cin,line);
      cerr << "Skipping line: " << line << endl;
    }
    else {
      cout << "INPUT ERROR\n";
      return -1;
    }
  }
  return -1;  
}

#endif
