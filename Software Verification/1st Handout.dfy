// David Mendes Nº44934

function sorted(a:array<int>, n:int):bool
    requires 0 <= n <= a.Length
    reads a
{ 
    forall i, j :: (0 <= i < j < n) ==> a[i] <= a[j]
}

function unique(a:array<int>, n:int):bool
    requires 0 <= n <= a.Length
    reads a
{ 
    forall i :: (0 <= i < n) ==> forall j :: (i < j < n) ==> a[i] != a[j]
}

function contains(b:array<int>, m:int, v:int):bool
    requires 0 <= m <= b.Length 
    reads b
{
    // exists l :: 0 <= l < m && b[l] == v
    v in b[..m]
}

// The commented invariants do not hold, but were intended to be verified
method Deduplicate(a:array<int>, n:int) returns (b:array<int>, m:int)
    requires 0 <= n <= a.Length
    requires sorted(a,n) 
    ensures 0 <= m <= b.Length
    ensures sorted(b,m) && unique(b,m)
    ensures forall k :: (0 <= k < m) ==> contains(a,n,b[k])
    // ensures forall i :: (0 <= i < n) ==> contains(b,m,a[i])
    ensures fresh(b)
{
    
    b := new int[n];

    if (n == 0) {
        return b,0;
    }

    b[0] := a[0];
    var i := 1;
    m:=1;

    assert contains(b,1,a[0]);

    while (i < n)
        decreases n-i
        invariant 1 <= i <= n
        invariant 1 <= m <= i 
        invariant sorted(b, m)
        invariant unique(b, m)
        // invariant forall j :: (0 <= j < i) ==> contains(b,m,a[j])
        invariant forall k :: (0 <= k < m) ==> contains(a,n,b[k])

        {
            if (a[i] > b[m-1]) {
                b[m] := a[i];
                m := m + 1;
                assert contains (b,m,a[i]); 
                
            }

            i := i + 1;
            
        }

}