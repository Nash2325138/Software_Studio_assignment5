HumanOCR (Collaborate Version):
	這次作業要沿用上次作業的成果，並改成連線版本，使兩個使用者連上伺服器，透過使用者輸入的比對來決定正確性。
	第一個遇到的問題是如何移植必須由 server 控制的程式碼，其實要改的只有決定使用哪個圖片的權力，在這次作業中要轉換給 server 操作，然後從一次兩個字改成一次一個字，隨機從 known 或 unknown 中挑選。
	
	再來是連線時最重要的問題：server, client 彼此間該如何溝通，使 client 端的遊戲 state 正確轉換？
	首先我先去定義 server 的狀態，總共有：BEGIN, BOTH_RUNNING, SINGLE_RUNNING, END
	如同字面上的意義，BEGIN 表示雙方的連線尚未都建立好的狀態（client 端至少一方還尚未執行程式），這時候就是純粹等待連線建立完畢。
	BOTH_RUNNING, SINGLE_RUNNING 分別代表：兩個 client 都尚未送出自己的答案、有其中一個已經送出答案而對方還沒。以下是這兩個 state 轉換的大致描述：

BOTH_RUNNING ------[當有一方 client 送出自己的答案被 server 讀到]------> 把答案存起來 ------> SINGLE_RUNNING
SINGLE_RUNNING ------[另一方 client 也送出了答案被讀到]------> 確認雙方答案是否一致，若 
1.不一樣：給兩個 client 送出 "Different" 
2.一樣：給兩個 client 送出 "Same"，並傳送 swapWord() 產生的 path of new word 給兩方的 clinet
------> BOTH_RUNNING，這樣 state 就會形成迴圈，直到 currentScore >= winScore

	再來解釋 client's gameStage 的狀態：BEGIN, RUNNING, WAITING, REPEAT, END
	BEGIN: 程式一執行就會是這個狀態，當收到 server 表示另一方連線建立完畢的訊息後，進入 RUNNING 進行遊戲，各種 thread 也在此時開始進行。
	RUNNING, WAITING, REPEAT 之間的關係用以下敘述來比達：
	
RUNNING ------[當使用者輸入答案並按下ENTER]------> WAITING
1.WAITING ------[若收到 server 的 "Different" 訊息]------> REPEAT，顯示答案錯誤訊息給使用者 ------[等2秒]------> RUNNING
2.WAITING ------[若收到 server 的 "Same" 訊息]------> 在讀一次訊息，這次訊息是新的字的路徑，利用此路徑讓 wordImg 正確載入下一個字 ------> RUNNING

	這樣就可以達成想要的樣子了，其餘部份就跟 assignment 4 一樣。