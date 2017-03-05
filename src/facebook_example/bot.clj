(ns facebook-example.bot
  (:gen-class)
  (:require [clojure.string :as s]
            [environ.core :refer [env]]
            [facebook-example.facebook :as fb]
            [facebook-example.actions :as actions]))

(defn on-message [payload]
  (println "on-message payload:")
  (println payload)
  (let [sender-id (get-in payload [:sender :id])
        recipient-id (get-in payload [:recipient :id])
        time-of-message (get-in payload [:timestamp])
        message-text (get-in payload [:message :text])]
    (cond
      (s/includes? (s/lower-case message-text) "help") (fb/send-message sender-id (fb/text-message "Hi there, happy to help :)"))
      (s/includes? (s/lower-case message-text) "image") (fb/send-message sender-id (fb/image-message "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/M101_hires_STScI-PRC2006-10a.jpg/1280px-M101_hires_STScI-PRC2006-10a.jpg"))
      ; If no rules apply echo the user's message-text input
      :else (fb/send-message sender-id (fb/text-message message-text)))))

(defn greet [sender-id first-name]
  (fb/send-message sender-id (fb/text-message (str "Welcome " first-name ", how are you today?")))
  (fb/send-message sender-id (fb/text-message "I am your buddy to challenge your limits. today we discover spots around the U6."))
  (fb/send-message sender-id (fb/button-message "what do you want to do?"
                                                [ { :type "postback"
                                                    :title "Start the game."
                                                    :payload "START"}

                                                  { :type "postback"
                                                    :title "I want to continue my journey."
                                                    :payload "CONTINUE"}])))

(defn send-start [sender-id]
  (fb/send-message sender-id (fb/text-message "bliblablu")))

(defn send-continue [sender-id]
  (fb/send-message sender-id (fb/text-message "bliblablu")))

(defn on-postback [payload]
  (println "on-postback payload:")
  (println payload)
  (let [sender-id (get-in payload [:sender :id])
        recipient-id (get-in payload [:recipient :id])
        time-of-message (get-in payload [:timestamp])
        postback (get-in payload [:postback :payload])
        referral (get-in payload [:postback :referral :ref])
        user-profile (fb/get-user-profile sender-id)]
    (cond
      (= postback "GET_STARTED") (greet sender-id (:first_name user-profile))
      (= postback "START") (send-start sender-id)
      (= postback "CONTINUE") (send-continue sender-id)
      :else (fb/send-message sender-id (fb/text-message "Sorry, I don't know how to handle that postback")))))

(defn on-location [sender-id attachment]
  (let [coordinates (get-in attachment [:payload :coordinates])]
    (actions/send-directions sender-id coordinates)))

(defn on-attachments [payload]
  (println "on-attachment payload:")
  (println payload)
  (let [sender-id (get-in payload [:sender :id])
        recipient-id (get-in payload [:recipient :id])
        time-of-message (get-in payload [:timestamp])
        attachments (get-in payload [:message :attachments])
        attachment (first attachments)]
    (cond
      (= (:type attachment) "location") (on-location sender-id attachment))
    (fb/send-message sender-id (fb/text-message "Thanks for your attachments :)"))))

(defn on-attachments [payload]
  (println "on-attachment payload:")
  (println payload)
  (let [sender-id (get-in payload [:sender :id])
        recipient-id (get-in payload [:recipient :id])
        time-of-message (get-in payload [:timestamp])
        attachments (get-in payload [:message :attachments])]
    (fb/send-message sender-id (fb/text-message "Thanks for your attachments :)"))))
