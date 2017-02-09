(ns facebook-example.facebook
  (:gen-class)
  (:require [clojure.string :as s]
            [facebook-example.messages :as msg]
            [environ.core :refer [env]]
            [facebook-example.fb-bot :as bot]))

(defn on-message [payload]
  (println "on-message")
  (println payload)
  (let [sender-id (get-in payload [:sender :id])
        recipient-id (get-in payload [:recipient :id])
        time-of-message (get-in payload [:timestamp])
        message (get-in payload [:message])
        message-text (get-in payload [:message :text])]
    (cond
      (s/includes? (s/lower-case message-text) "help") (bot/send-message sender-id (bot/text-message "Hi there, happy to help :)"))
      (s/includes? (s/lower-case message-text) "image") (bot/send-message sender-id (bot/image-message "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/M101_hires_STScI-PRC2006-10a.jpg/1280px-M101_hires_STScI-PRC2006-10a.jpg"))
      ;(s/includes? (s/lower-case message-text) "quick reply") (msg/sendQuickReply [sender-id])
      ; If no rules apply echo the user's message-text input
      :else (bot/send-message sender-id (bot/text-message message-text)))))

(defn on-postback [payload]
  (println "on-postback")
  (println payload))

(defn on-attachment [payload])
